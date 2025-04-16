package com.deeptech.deeptech_test.Model;

import com.deeptech.deeptech_test.Enum.JenisKelamin;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "admin")
@Getter
@Setter
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama_depan", nullable = false, length = 50)
    private String namaDepan;

    @Column(name = "nama_belakang", nullable = false, length = 50)
    private String namaBelakang;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "tanggal_lahir", nullable = false)
    private LocalDate tanggalLahir;

    @Enumerated(EnumType.STRING)
    @Column(name = "jenis_kelamin", nullable = false)
    private JenisKelamin jenisKelamin;

    @Column(nullable = false)
    private String password;

}
