package com.deeptech.deeptech_test.Dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CutiResponseDto {

    private Long id;
    private String alasan;
    private LocalDate tanggalMulai;
    private LocalDate tanggalSelesai;
}
