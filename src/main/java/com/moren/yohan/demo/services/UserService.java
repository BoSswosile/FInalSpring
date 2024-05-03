package com.moren.yohan.demo.services;

import com.moren.yohan.demo.models.Admin;
import com.moren.yohan.demo.models.Role;
import com.moren.yohan.demo.models.User;

import java.util.Optional;

public interface UserService {
    void addRoleToUser(User user, Role role);
    User createUser(User entity);

    User updateUser(User entity);

    Optional<User> getUserById(Long id);

    void deleteUser(User user);
}
