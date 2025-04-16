package com.deeptech.deeptech_test.Dto;

import com.deeptech.deeptech_test.Enum.JenisKelamin;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AdminCreateRequestDto {

    @NotBlank(message = "Nama depan cannot be blank")
    private String namaDepan;

    @NotBlank(message = "Nama belakang cannot be blank")
    private String namaBelakang;

    @NotBlank(message = "Email tidak cannot be blank")
    @Email()
    private String email;

    @NotNull(message = "Tanggal lahir cannot be null")
    @Past(message = "Tanggal lahir must past time")
    private LocalDate tanggalLahir;

    @NotNull(message = "Jenis kelamin cannot be null")
    private JenisKelamin jenisKelamin;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password min 6 character")
    private String password;
}
