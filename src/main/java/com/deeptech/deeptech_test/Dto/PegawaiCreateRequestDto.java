package com.deeptech.deeptech_test.Dto;

import com.deeptech.deeptech_test.Enum.JenisKelamin;
import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class PegawaiCreateRequestDto {

    @NotBlank(message = "Nama depan cannot be blank")
    private String namaDepan;

    @NotBlank(message = "Nama belakang cannot be blank")
    private String namaBelakang;

    @NotBlank(message = "Email tidak cannot be blank")
    @Email()
    private String email;

    @NotNull(message = "nomor hp cannot be null")
    @Pattern(regexp = "^[0-9]+$", message = "Phone number must contain only digits")
    private String nomorHp;

    @NotNull(message = "alamat cannot be null")
    private String alamat;

    @NotNull(message = "Jenis kelamin cannot be null")
    private JenisKelamin jenisKelamin;
}
