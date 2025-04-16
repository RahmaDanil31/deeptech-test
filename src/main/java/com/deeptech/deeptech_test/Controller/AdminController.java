package com.deeptech.deeptech_test.Controller;

import com.deeptech.deeptech_test.BaseAsset.ResponseData;
import com.deeptech.deeptech_test.BaseAsset.ResponseDataList;
import com.deeptech.deeptech_test.Dto.AdminCreateRequestDto;
import com.deeptech.deeptech_test.Dto.AdminResponseDto;
import com.deeptech.deeptech_test.Dto.AdminSearchRequestDto;
import com.deeptech.deeptech_test.Dto.AdminUpdateRequestDto;
import com.deeptech.deeptech_test.Service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping()
    public ResponseEntity<ResponseDataList<AdminResponseDto>> search(AdminSearchRequestDto adminSearchRequestDto){
        ResponseDataList<AdminResponseDto> responseList = adminService.search(adminSearchRequestDto);
        return ResponseEntity.status(200).body(responseList);
    }

    @PostMapping()
    public ResponseEntity<ResponseData<AdminResponseDto>> save(@Valid @RequestBody AdminCreateRequestDto adminCreateRequestDto){
        AdminResponseDto savedUser = adminService.save(adminCreateRequestDto);
        ResponseData<AdminResponseDto> response = new ResponseData<>(savedUser, HttpStatus.CREATED.getReasonPhrase(),201);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<AdminResponseDto>> get(@PathVariable Long id) {
        AdminResponseDto adminResponseDto = adminService.get(id);
        ResponseData<AdminResponseDto> response = new ResponseData<>(adminResponseDto, HttpStatus.OK.getReasonPhrase(),200);
        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<AdminResponseDto>> update(
            @PathVariable Long id,
            @Valid @RequestBody AdminUpdateRequestDto adminUpdateRequestDto) {
        AdminResponseDto updatedOrder = adminService.update(id, adminUpdateRequestDto);
        ResponseData<AdminResponseDto> response = new ResponseData<>(updatedOrder, HttpStatus.OK.getReasonPhrase(),200);
        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<String>> delete(@PathVariable Long id) {
        ResponseData<String> response = new ResponseData<>(null, HttpStatus.NO_CONTENT.getReasonPhrase(), 204);
        adminService.delete(id);
        return ResponseEntity.status(204).body(response);
    }
}
