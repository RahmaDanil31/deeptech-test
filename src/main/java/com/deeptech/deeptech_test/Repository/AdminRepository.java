package com.deeptech.deeptech_test.Repository;

import com.deeptech.deeptech_test.Model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> , JpaSpecificationExecutor<Admin> {

    @Query("select a from Admin a where a.email = ?1")
    Admin findByEmail(String email);
}
