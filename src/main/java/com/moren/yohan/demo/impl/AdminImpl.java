package com.moren.yohan.demo.impl;

import com.moren.yohan.demo.models.Admin;
import com.moren.yohan.demo.models.Role;
import com.moren.yohan.demo.repositories.AdminRepo;
import com.moren.yohan.demo.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminImpl implements AdminService {

    @Autowired
    AdminRepo adminRepo;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void addRoleToAdmin(Admin user, Role role){
        user.addRole(role);
        adminRepo.save(user);
    }


    @Override
    public Admin createAdmin(Admin admin) {
        return adminRepo.save(admin);
    }
}
