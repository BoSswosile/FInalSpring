package com.moren.yohan.demo.repositories;

import com.moren.yohan.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    User save(User user);

    Optional<User> findByEmail(String email);

    void delete(User user);

    Optional<User> findById(Long id);
}
