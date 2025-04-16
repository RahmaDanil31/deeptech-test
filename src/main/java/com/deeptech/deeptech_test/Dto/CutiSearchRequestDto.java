package com.deeptech.deeptech_test.Dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CutiSearchRequestDto extends PageRequestDto{
    private String alasan;
    private LocalDate tanggalMulai;
    private LocalDate tanggalSelesai;
}
