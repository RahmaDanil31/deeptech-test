package com.deeptech.deeptech_test.Repository;

import com.deeptech.deeptech_test.Model.Admin;
import com.deeptech.deeptech_test.Model.Cuti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CutiRepository extends JpaRepository<Cuti,Long> , JpaSpecificationExecutor<Cuti> {
    List<Cuti> findByPegawaiId(Long pegawaiId);

    @Query("SELECT c FROM Cuti c WHERE c.pegawai.id = :pegawaiId AND YEAR(c.tanggalMulai) = :tahunMulai OR YEAR(c.tanggalSelesai) = :tanggalSelesai")
    List<Cuti> findByPegawaiIdAndYear(@Param("pegawaiId") Long pegawaiId, @Param("tahunMulai") int tahunMulai,@Param("tanggalSelesai") int tanggalSelesai);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Cuti c WHERE c.pegawai.id = :pegawaiId " +
            "AND MONTH(c.tanggalMulai) = :month AND YEAR(c.tanggalMulai) = :year")
    boolean existsByPegawaiIdAndMonthYear(
            @Param("pegawaiId") Long pegawaiId,
            @Param("month") int month,
            @Param("year") int year
    );

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Cuti c WHERE c.pegawai.id = :pegawaiId " +
            "AND MONTH(c.tanggalSelesai) = :month AND YEAR(c.tanggalSelesai) = :year")
    boolean existsByPegawaiIdAndMonthYear2(
            @Param("pegawaiId") Long pegawaiId,
            @Param("month") int month,
            @Param("year") int year
    );

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cuti c WHERE c.pegawai.id = :pegawaiId AND MONTH(c.tanggalMulai) = :month AND YEAR(c.tanggalMulai) = :year AND c.id <> :cutiId")
    boolean existsByPegawaiIdAndMonthYearExcludeId(Long pegawaiId, int month, int year, Long cutiId);

    Optional<Cuti> findTopByPegawaiIdOrderByTanggalMulaiDesc(Long pegawaiId);
}
