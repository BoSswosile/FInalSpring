package com.moren.yohan.demo.controllers;

import com.moren.yohan.demo.models.Admin;
import com.moren.yohan.demo.models.User;
import com.moren.yohan.demo.repositories.UserRepo;
import com.moren.yohan.demo.services.AuthService;
import com.moren.yohan.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
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
    public ResponseEntity<?> updateUserCredentials(@RequestBody User user, @RequestBody String newPassword) {
        if (user == null) {
            return new ResponseEntity<>("User is not valid", HttpStatus.BAD_REQUEST);
        }
        // Need to compare both passwords
        Optional<User> userToUpdate = userRepo.findByEmail(user.getEmail());
        if (userToUpdate.isEmpty()) {
            return new ResponseEntity<>("User does not exist", HttpStatus.NOT_FOUND);
        }
        if (!bCryptPasswordEncoder.matches(user.getPassword(), userToUpdate.get().getPassword())) {
            return new ResponseEntity<>("Passwords do not match", HttpStatus.BAD_REQUEST);
        }
        user.setPassword(newPassword);
        return new ResponseEntity<>(userService.updateUser(user), HttpStatus.OK);
    }
}
