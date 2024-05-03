package com.moren.yohan.demo.controllers;

import com.moren.yohan.demo.models.Admin;
import com.moren.yohan.demo.models.Role;
import com.moren.yohan.demo.models.User;
import com.moren.yohan.demo.repositories.AdminRepo;
import com.moren.yohan.demo.repositories.RoleRepo;
import com.moren.yohan.demo.requests.LoginRequest;
import com.moren.yohan.demo.services.AdminService;
import com.moren.yohan.demo.services.AuthService;
import com.moren.yohan.demo.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("admin")
public class AdminController {

    @Autowired
    AdminRepo adminRepo;
    @Autowired
    AuthService authService;
    @Autowired
    RoleRepo roleRepo;
    @Autowired
    UserService userService;


    @PostMapping("/signup")
    public ResponseEntity<?> adminSignUp(@org.springframework.web.bind.annotation.RequestBody Admin admin, BindingResult bindingResult) {
        System.out.print(admin);
        Optional<Role> role = roleRepo.findByRoleName(Role.RoleEnum.ADMIN.name());
        Optional<Admin> adminExists = adminRepo.findByEmail(admin.getEmail());
        if (adminExists.isPresent()) return new ResponseEntity<>("Admin already exists", HttpStatus.CONFLICT);
        if (role.isEmpty()) return new ResponseEntity<>("An error occurred!", HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(authService.registerAdmin(admin, role.get()), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Login with email and password", requestBody = @RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginRequest.class))))
    public ResponseEntity<?> login(@org.springframework.web.bind.annotation.RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        Optional<Admin> admin = adminRepo.findByEmail(email);
        if (admin.isEmpty()) return new ResponseEntity<>("Admin does not exist", HttpStatus.NOT_FOUND);
        String jwt = authService.login(admin.get(), password);
        if (jwt == null) return new ResponseEntity<>("Mot de passe incorrect", HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(jwt, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/users")
    public ResponseEntity<?> createUsers(@Valid @org.springframework.web.bind.annotation.RequestBody User user, BindingResult bindingResult) {
        Optional<Role> role = roleRepo.findByRoleName(Role.RoleEnum.USER.name());
        if (role.isEmpty()) return new ResponseEntity<>("An error occurred!", HttpStatus.INTERNAL_SERVER_ERROR);
        List<ObjectError> errors = bindingResult.getAllErrors();
        for (ObjectError error : errors) {
            if (error.getDefaultMessage() != null)
                return new ResponseEntity<>(error.getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(authService.registerUser(user, role.get()), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUsers(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isEmpty()) return new ResponseEntity<>("User does not exist", HttpStatus.NOT_FOUND);
        userService.deleteUser(user.get());
        return new ResponseEntity<>("User deleted", HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUsers(@PathVariable Long id, @org.springframework.web.bind.annotation.RequestBody User user) {
        Optional<User> userExists = userService.getUserById(id);
        if (userExists.isEmpty()) return new ResponseEntity<>("User does not exist", HttpStatus.NOT_FOUND);
        user.setId(id);
        return new ResponseEntity<>(userService.updateUser(user), HttpStatus.OK);
    }
}