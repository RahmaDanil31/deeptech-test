package com.deeptech.deeptech_test.Service;

import com.deeptech.deeptech_test.BaseAsset.ResponseDataList;
import com.deeptech.deeptech_test.Dto.AdminResponseDto;
import com.deeptech.deeptech_test.Dto.PegawaiCreateRequestDto;
import com.deeptech.deeptech_test.Dto.PegawaiResponseDto;
import com.deeptech.deeptech_test.Dto.PegawaiSearchRequestDto;
import com.deeptech.deeptech_test.Exception.DataNotFoundException;
import com.deeptech.deeptech_test.Exception.EmailAlreadyExistsException;
import com.deeptech.deeptech_test.Model.Admin;
import com.deeptech.deeptech_test.Model.Pegawai;
import com.deeptech.deeptech_test.Repository.PegawaiRepository;
import com.deeptech.deeptech_test.Util.MetadataMapperUtil;
import com.deeptech.deeptech_test.Util.ModelMapperUtil;
import com.deeptech.deeptech_test.Util.SearchingUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PegawaiService {

private final PegawaiRepository pegawaiRepository;

    @Transactional
    public PegawaiResponseDto save(PegawaiCreateRequestDto pegawaiCreateRequestDto) {
        if(pegawaiRepository.findByEmail(pegawaiCreateRequestDto.getEmail())!=null)
            throw new EmailAlreadyExistsException("Email is already in exist");

        Pegawai newPegawai = ModelMapperUtil.convert(pegawaiCreateRequestDto, Pegawai.class);
        Pegawai savedPegawai = pegawaiRepository.save(newPegawai);
        return ModelMapperUtil.convert(savedPegawai, PegawaiResponseDto.class);

    }

    @Transactional
    public PegawaiResponseDto update(Long id, @Valid PegawaiCreateRequestDto pegawaiCreateRequestDto) {
        getById(id);
        Pegawai updatePegawai = ModelMapperUtil.convert(pegawaiCreateRequestDto, Pegawai.class);
        updatePegawai.setId(id);
        Pegawai updatedPegawai = pegawaiRepository.save(updatePegawai);

        return ModelMapperUtil.convert(updatedPegawai, PegawaiResponseDto.class);
    }

    public Pegawai getById(Long id) {

        Pegawai pegawai = pegawaiRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("User with id " + id + " not found"));
        return pegawai;
    }

    public void delete(Long id) {
        Pegawai pegawai = getById(id);
        pegawaiRepository.delete(pegawai);
    }

    public PegawaiResponseDto get(Long id) {
        Pegawai pegawai = getById(id);
        return ModelMapperUtil.convert(pegawai, PegawaiResponseDto.class);
    }

    public ResponseDataList<Pegawai> search(PegawaiSearchRequestDto pegawaiSearchRequestDto) {

        Pageable pageable = PageRequest.of(
                pegawaiSearchRequestDto.getPage(),
                pegawaiSearchRequestDto.getLimit(),
                Sort.by(
                        Sort.Direction.fromString(pegawaiSearchRequestDto.getSortDirection()),
                        pegawaiSearchRequestDto.getSortBy()
                )
        );

        Page<Pegawai> pegawais = pegawaiRepository.findAll(SearchingUtil.build(pegawaiSearchRequestDto), pageable);

        return new ResponseDataList<>(
                MetadataMapperUtil.buildMetadata(pegawais),
                pegawais.getContent()
        );
    }
}
