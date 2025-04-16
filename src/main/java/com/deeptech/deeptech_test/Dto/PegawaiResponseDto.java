package com.deeptech.deeptech_test.Dto;

import com.deeptech.deeptech_test.Enum.JenisKelamin;
import lombok.Data;

@Data
public class PegawaiResponseDto {
    private Long id;
    private String namaDepan;
    private String namaBelakang;
    private String email;
    private String nomorHp;
    private String alamat;
    private JenisKelamin jenisKelamin;
}
