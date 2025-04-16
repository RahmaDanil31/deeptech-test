package com.deeptech.deeptech_test.Model;

import com.deeptech.deeptech_test.Dto.CutiResponseDto;
import com.deeptech.deeptech_test.Enum.JenisKelamin;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pegawai")
@Getter
@Setter
public class Pegawai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama_depan", nullable = false, length = 50)
    private String namaDepan;

    @Column(name = "nama_belakang", nullable = false, length = 50)
    private String namaBelakang;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String nomorHp;

    @Column(nullable = false, length = 250)
    private String alamat;

    @Enumerated(EnumType.STRING)
    @Column(name = "jenis_kelamin", nullable = false)
    private JenisKelamin jenisKelamin;

    @JsonIgnore
    @OneToMany(mappedBy = "pegawai", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cuti> cuties;

    @JsonProperty("daftarCuti")
    public List<CutiResponseDto> getCuties(){
        List<CutiResponseDto> cutiResponseDtos = new ArrayList<>();

        for(Cuti cuti:cuties){
            CutiResponseDto cutiResponseDto = new CutiResponseDto();
            cutiResponseDto.setId(cuti.getId());
            cutiResponseDto.setAlasan(cuti.getAlasan());
            cutiResponseDto.setTanggalMulai(cuti.getTanggalMulai());
            cutiResponseDto.setTanggalSelesai(cuti.getTanggalSelesai());

            cutiResponseDtos.add(cutiResponseDto);
        }
        return  cutiResponseDtos;
    }


}
