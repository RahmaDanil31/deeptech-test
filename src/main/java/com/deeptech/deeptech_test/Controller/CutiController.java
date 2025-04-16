package com.deeptech.deeptech_test.Controller;

import com.deeptech.deeptech_test.BaseAsset.ResponseData;
import com.deeptech.deeptech_test.BaseAsset.ResponseDataList;
import com.deeptech.deeptech_test.Dto.*;
import com.deeptech.deeptech_test.Model.Cuti;
import com.deeptech.deeptech_test.Service.CutiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cuti")
@RequiredArgsConstructor
public class CutiController {

    private final CutiService cutiService;

    @GetMapping()
    public ResponseEntity<ResponseDataList<Cuti>> search(CutiSearchRequestDto cutiSearchRequestDto){
        ResponseDataList<Cuti> responseList = cutiService.search(cutiSearchRequestDto);
        return ResponseEntity.status(200).body(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<CutiResponseDto>> get(@PathVariable Long id) {
        CutiResponseDto cutiResponseDto = cutiService.get(id);
        ResponseData<CutiResponseDto> response = new ResponseData<>(cutiResponseDto, HttpStatus.OK.getReasonPhrase(),200);
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping
    public ResponseEntity<ResponseData<CutiResponseDto>> createCuti(@RequestBody @Valid CutiCreateRequestDto cutiCreateRequestDto) throws Exception {
        CutiResponseDto newCuti = cutiService.createCuti(cutiCreateRequestDto);
        ResponseData<CutiResponseDto> response = new ResponseData<>(newCuti, HttpStatus.CREATED.getReasonPhrase(),201);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping()
    public ResponseEntity<ResponseData<CutiResponseDto>> updateCutiTerakhir(@RequestBody CutiCreateRequestDto dto) throws Exception {
        CutiResponseDto updatedCuti = cutiService.updateCutiTerakhir(dto);
        ResponseData<CutiResponseDto> response = new ResponseData<>(updatedCuti, HttpStatus.OK.getReasonPhrase(),200);
        return ResponseEntity.status(200).body(response);
    }


    @DeleteMapping("/{pegawaiId}")
    public ResponseEntity<ResponseData<String>> deleteCutiTerakhir(@PathVariable Long pegawaiId) throws Exception {
        ResponseData<String> response = new ResponseData<>(null, HttpStatus.NO_CONTENT.getReasonPhrase(), 204);
        cutiService.deleteCutiTerakhir(pegawaiId);
        return ResponseEntity.status(204).body(response);
    }
}
