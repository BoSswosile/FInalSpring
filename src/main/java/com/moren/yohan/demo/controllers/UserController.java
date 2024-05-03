package com.moren.yohan.demo.controllers;

import com.moren.yohan.demo.models.Admin;
import com.moren.yohan.demo.models.User;
import com.moren.yohan.demo.repositories.UserRepo;
import com.moren.yohan.demo.requests.ChangePasswordRequest;
import com.moren.yohan.demo.requests.LoginRequest;
import com.moren.yohan.demo.security.JwtService;
import com.moren.yohan.demo.services.AuthService;
import com.moren.yohan.demo.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;

    @Autowired
    UserRepo userRepo;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    JwtService jwtService;


    @PostMapping("/login")
    @Operation(summary = "Login", description = "Login with email and password", requestBody = @RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginRequest.class))))
    public ResponseEntity<?> login(@org.springframework.web.bind.annotation.RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        Optional<User> user = userRepo.findByEmail(email);
        if (user.isEmpty()) return new ResponseEntity<>("User does not exist", HttpStatus.NOT_FOUND);
        String jwt = authService.login(user.get(), password);
        if (jwt == null) return new ResponseEntity<>("Mot de passe incorrect", HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(jwt, HttpStatus.OK);
    }

    @GetMapping("/validate/{email}/{code}")
    public ResponseEntity<?> validateAccount(@PathVariable String email, @PathVariable String code) {
        if (email == null || code == null) {
            return new ResponseEntity<>("Email or code not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userService.validateAccount(email, code), HttpStatus.OK);
    }

    // does not work because of the empty password
    @PutMapping("/users")
    @Operation(summary = "ResetPassword", description = "Change password using the old password and the new password", requestBody = @RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChangePasswordRequest.class))))
    public ResponseEntity<?> updateUserCredentials(@RequestHeader("Authorization") String jwt, @org.springframework.web.bind.annotation.RequestBody Map<String, String> request) {
        String password = request.get("password");
        String newPassword = request.get("newPassword");
        if (password == null || newPassword == null) {
            return new ResponseEntity<>("Password or new password not found", HttpStatus.BAD_REQUEST);
        }
        jwt = jwt.replace("Bearer ", "");
        String email = jwtService.extractUsername(jwt);
        Optional<User> userToUpdate = userRepo.findByEmail(email);
        if (userToUpdate.isEmpty()) {
            return new ResponseEntity<>("User does not exist", HttpStatus.NOT_FOUND);
        }
        if (!bCryptPasswordEncoder.matches(password, userToUpdate.get().getPassword())) {
            return new ResponseEntity<>("Passwords do not match", HttpStatus.BAD_REQUEST);
        }
        userToUpdate.get().setPassword(bCryptPasswordEncoder.encode(newPassword));
        return new ResponseEntity<>(userService.updateUser(userToUpdate.get()), HttpStatus.OK);
    }
}
