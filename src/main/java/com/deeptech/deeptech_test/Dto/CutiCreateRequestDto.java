package com.deeptech.deeptech_test.Dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
public class CutiCreateRequestDto {

    @NotNull(message = "pegawai id cannot be null")
    private Long pegawaiId;

    @NotBlank(message = "alasan cannot be blank")
    private String alasan;

    @NotNull(message = "tanggal Mulai cannot be null")
    @Future(message = "tanggal Mulai must future time")
    private LocalDate tanggalMulai;

    @NotNull(message = "Tanggal Selesai cannot be null")
    @Future(message = "tanggal Selesai must future time")
    private LocalDate tanggalSelesai;

    public int getDurasiHari() {
        return (int) ChronoUnit.DAYS.between(tanggalMulai, tanggalSelesai) + 1;
    }
}
