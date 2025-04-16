package com.deeptech.deeptech_test.Controller;

import com.deeptech.deeptech_test.BaseAsset.ResponseData;
import com.deeptech.deeptech_test.BaseAsset.ResponseDataList;
import com.deeptech.deeptech_test.Dto.*;
import com.deeptech.deeptech_test.Model.Pegawai;
import com.deeptech.deeptech_test.Service.PegawaiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pegawai")
@RequiredArgsConstructor
public class PegawaiController {

    private final PegawaiService pegawaiService;

    @GetMapping()
    public ResponseEntity<ResponseDataList<Pegawai>> search(PegawaiSearchRequestDto pegawaiSearchRequestDto){
        ResponseDataList<Pegawai> responseList = pegawaiService.search(pegawaiSearchRequestDto);
        return ResponseEntity.status(200).body(responseList);
    }

    @PostMapping()
    public ResponseEntity<ResponseData<PegawaiResponseDto>> save(@Valid @RequestBody PegawaiCreateRequestDto pegawaiCreateRequestDto){
        PegawaiResponseDto savedPegawai = pegawaiService.save(pegawaiCreateRequestDto);
        ResponseData<PegawaiResponseDto> response = new ResponseData<>(savedPegawai, HttpStatus.CREATED.getReasonPhrase(),201);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<PegawaiResponseDto>> get(@PathVariable Long id) {
        PegawaiResponseDto pegawaiResponseDto = pegawaiService.get(id);
        ResponseData<PegawaiResponseDto> response = new ResponseData<>(pegawaiResponseDto, HttpStatus.OK.getReasonPhrase(),200);
        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<PegawaiResponseDto>> update(
            @PathVariable Long id,
            @Valid @RequestBody PegawaiCreateRequestDto pegawaiCreateRequestDto) {
        PegawaiResponseDto updatedPegawai = pegawaiService.update(id, pegawaiCreateRequestDto);
        ResponseData<PegawaiResponseDto> response = new ResponseData<>(updatedPegawai, HttpStatus.OK.getReasonPhrase(),200);
        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<String>> delete(@PathVariable Long id) {
        ResponseData<String> response = new ResponseData<>(null, HttpStatus.NO_CONTENT.getReasonPhrase(), 204);
        pegawaiService.delete(id);
        return ResponseEntity.status(204).body(response);
    }
}
