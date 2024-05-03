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
        int random = (int) (Math.random() * (999999 - 100000 + 1) + 100000);
        entity.setValidateCode(String.valueOf(random));
        System.out.println(" VALIDATE YOUR ACCOUNT LINK -> " + "localhost:8080/user/validate/" + entity.getEmail() + "/" + entity.getValidateCode());
        // send mail
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

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public String validateAccount(String email, String code) {
        Optional<User> user = findByEmail(email);
        if (user.isEmpty()) {
            return "Email not found";
        }
        if (!user.get().getValidateCode().equals(code)) {
            return "Code not valid or expired";
        }
        user.get().setIsAccountValid(true);
        user.get().setValidateCode(null);
        userRepo.save(user.get());
        return "Account validated";
    }
    @Override
    public User save(User user) {
        return userRepo.save(user);
    }
}
