package com.moren.yohan.demo.repositories;

import com.moren.yohan.demo.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepo extends JpaRepository<Admin, Long> {

    Admin save(Admin admin);

    Optional<Admin> findByEmail(String email);
}
