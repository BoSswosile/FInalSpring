package com.moren.yohan.demo.services;

import com.moren.yohan.demo.models.Admin;
import com.moren.yohan.demo.models.Role;
import com.moren.yohan.demo.models.User;
import org.springframework.beans.factory.annotation.Autowired;

public interface AdminService {

    void addRoleToAdmin(Admin user, Role role);

    Admin createAdmin(Admin admin);

}
