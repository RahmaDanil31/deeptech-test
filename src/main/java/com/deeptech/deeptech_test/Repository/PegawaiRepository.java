package com.deeptech.deeptech_test.Repository;

import com.deeptech.deeptech_test.Model.Admin;
import com.deeptech.deeptech_test.Model.Pegawai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PegawaiRepository extends JpaRepository<Pegawai,Long>, JpaSpecificationExecutor<Pegawai> {

    @Query("select p from Pegawai p where p.email = ?1")
    Admin findByEmail(String email);
}
