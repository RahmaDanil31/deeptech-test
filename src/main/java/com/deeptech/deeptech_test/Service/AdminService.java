package com.deeptech.deeptech_test.Service;

import com.deeptech.deeptech_test.BaseAsset.ResponseDataList;
import com.deeptech.deeptech_test.Dto.AdminCreateRequestDto;
import com.deeptech.deeptech_test.Dto.AdminResponseDto;
import com.deeptech.deeptech_test.Dto.AdminSearchRequestDto;
import com.deeptech.deeptech_test.Dto.AdminUpdateRequestDto;
import com.deeptech.deeptech_test.Exception.DataNotFoundException;
import com.deeptech.deeptech_test.Exception.EmailAlreadyExistsException;
import com.deeptech.deeptech_test.Model.Admin;
import com.deeptech.deeptech_test.Repository.AdminRepository;
import com.deeptech.deeptech_test.Util.MetadataMapperUtil;
import com.deeptech.deeptech_test.Util.ModelMapperUtil;
import com.deeptech.deeptech_test.Util.PasswordEncodeUtil;
import com.deeptech.deeptech_test.Util.SearchingUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    public Admin getByEmail(String email){
        return adminRepository.findByEmail(email);
    }

    @Transactional
    public AdminResponseDto save(AdminCreateRequestDto adminCreateRequestDto) {

        if(adminRepository.findByEmail(adminCreateRequestDto.getEmail())!=null)
            throw new EmailAlreadyExistsException("Email is already in exist");

        Admin newAdmin = ModelMapperUtil.convert(adminCreateRequestDto, Admin.class);
        newAdmin.setPassword(PasswordEncodeUtil.create(adminCreateRequestDto.getPassword()));
        Admin savedAdmin = adminRepository.save(newAdmin);
        return ModelMapperUtil.convert(savedAdmin, AdminResponseDto.class);
    }

    public Admin getById(Long id) {

        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("User with id " + id + " not found"));
        return admin;
    }


    public void delete(Long id) {
        Admin admin = getById(id);
        adminRepository.delete(admin);
    }

    @Transactional
    public AdminResponseDto update(Long id,AdminUpdateRequestDto adminUpdateRequestDto) {
        Admin admin = getById(id);
        Admin updateAdmin = ModelMapperUtil.convert(adminUpdateRequestDto, Admin.class);
        updateAdmin.setId(id);
        updateAdmin.setPassword(admin.getPassword());
        Admin updatedAdmin = adminRepository.save(updateAdmin);

        return ModelMapperUtil.convert(updatedAdmin, AdminResponseDto.class);
    }

    public AdminResponseDto get(Long id) {
        Admin admin = getById(id);
        return ModelMapperUtil.convert(admin, AdminResponseDto.class);
    }

    public ResponseDataList<AdminResponseDto> search(AdminSearchRequestDto adminSearchRequestDto) {
        Pageable pageable = PageRequest.of(
                adminSearchRequestDto.getPage(),
                adminSearchRequestDto.getLimit(),
                Sort.by(
                        Sort.Direction.fromString(adminSearchRequestDto.getSortDirection()),
                        adminSearchRequestDto.getSortBy()
                )
        );

        Page<Admin> admins = adminRepository.findAll(SearchingUtil.build(adminSearchRequestDto), pageable);

        return new ResponseDataList<>(
                MetadataMapperUtil.buildMetadata(admins),
                ModelMapperUtil.mapList(admins.getContent(), AdminResponseDto.class)
        );
    }
}
