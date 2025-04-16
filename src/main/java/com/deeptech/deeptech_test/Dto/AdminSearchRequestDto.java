package com.deeptech.deeptech_test.Dto;

import com.deeptech.deeptech_test.Enum.JenisKelamin;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AdminSearchRequestDto extends PageRequestDto{
    private String namaDepan;
    private String namaBelakang;
    private String email;
    private LocalDate tanggalLahir;
    private JenisKelamin jenisKelamin;
}
