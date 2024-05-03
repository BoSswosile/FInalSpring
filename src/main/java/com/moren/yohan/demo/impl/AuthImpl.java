package com.moren.yohan.demo.impl;

import com.moren.yohan.demo.models.Admin;
import com.moren.yohan.demo.models.Role;
import com.moren.yohan.demo.models.User;
import com.moren.yohan.demo.security.JwtService;
import com.moren.yohan.demo.services.AdminService;
import com.moren.yohan.demo.services.AuthService;
import com.moren.yohan.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthImpl implements AuthService {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    UserService userService;

    @Autowired
    AdminService adminService;

    @Autowired
    JwtService jwtService;

    @Override
    public String login(Admin user, String password) {
        if(bCryptPasswordEncoder.matches(password, user.getPassword()))
            return jwtService.generateToken(user);
        return null;
    }

    @Override
    public String login(User user, String password) {
        if(bCryptPasswordEncoder.matches(password, user.getPassword()))
            return jwtService.generateToken(user);
        return null;
    }

    @Override
    public User registerUser(User entity, Role role) {
        String passwordEncoded = bCryptPasswordEncoder.encode(entity.getPassword());
        entity.setPassword(passwordEncoded);
        userService.addRoleToUser(entity, role);
        return userService.createUser(entity);
    }

    @Override
    public Admin registerAdmin(Admin admin, Role role) {
        String passwordEncoded = bCryptPasswordEncoder.encode(admin.getPassword());
        admin.setPassword(passwordEncoded);
        adminService.addRoleToAdmin(admin, role);
        return adminService.createAdmin(admin);
    }
}
