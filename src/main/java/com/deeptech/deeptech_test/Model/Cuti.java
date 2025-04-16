package com.deeptech.deeptech_test.Model;

import com.deeptech.deeptech_test.Dto.PegawaiResponseDto;
import com.deeptech.deeptech_test.Util.ModelMapperUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "cuti")
@Getter
@Setter
public class Cuti {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alasan", nullable = false)
    private String alasan;

    @Column(name = "tanggal_mulai", nullable = false)
    private LocalDate tanggalMulai;

    @Column(name = "tanggal_selesai", nullable = false)
    private LocalDate tanggalSelesai;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "pegawai_id")
    private Pegawai pegawai;

    public PegawaiResponseDto getPegawai(){
        return ModelMapperUtil.convert(pegawai,PegawaiResponseDto.class);
    }

    @JsonIgnore
    public int getDurasiHari() {
        if(tanggalMulai.getYear()!=tanggalSelesai.getYear())
            return (int) ChronoUnit.DAYS.between(tanggalMulai, tanggalSelesai);

        return (int) ChronoUnit.DAYS.between(tanggalMulai, tanggalSelesai)+1;
    }
}
