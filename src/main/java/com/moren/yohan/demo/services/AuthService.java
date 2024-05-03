package com.moren.yohan.demo.services;

import com.moren.yohan.demo.models.Admin;
import com.moren.yohan.demo.models.Role;
import com.moren.yohan.demo.models.User;

public interface AuthService {
    String login(Admin admin, String password);
    User registerUser(User entity, Role role);

    Admin registerAdmin(Admin admin, Role role);

}
