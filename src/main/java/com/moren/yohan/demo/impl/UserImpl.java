package com.moren.yohan.demo.impl;

import com.moren.yohan.demo.models.Role;
import com.moren.yohan.demo.models.User;
import com.moren.yohan.demo.repositories.UserRepo;
import com.moren.yohan.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserImpl implements UserService {
    @Autowired
    UserRepo userRepo;

    @Override
    public void addRoleToUser(User user, Role role){
        user.addRole(role);
        userRepo.save(user);
    }

    @Override
    public User createUser(User entity) {
        return userRepo.save(entity);
    }

    @Override
    public User updateUser(User entity) {
        return userRepo.save(entity);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepo.findById(id);
    }

    @Override
    public void deleteUser(User user) {
        userRepo.delete(user);
    }
}
