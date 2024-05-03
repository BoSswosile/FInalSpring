package com.moren.yohan.demo.config;

import com.moren.yohan.demo.models.Role;
import com.moren.yohan.demo.repositories.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInit implements CommandLineRunner {
    @Autowired
    RoleRepo roleRepo;

    @Override
    public void run(String... args) throws Exception {
        for (Role.RoleEnum roleEnum : Role.RoleEnum.values())   {
            roleRepo.save(new Role(null, roleEnum.name()));
        }
    }
}
