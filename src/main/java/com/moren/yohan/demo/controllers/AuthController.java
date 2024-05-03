package com.moren.yohan.demo.controllers;

import com.moren.yohan.demo.models.Role;
import com.moren.yohan.demo.models.User;
import com.moren.yohan.demo.repositories.RoleRepo;
import com.moren.yohan.demo.services.AuthService;
import com.moren.yohan.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    UserService userService;
    @Autowired
    AuthService authService;
    @Autowired
    RoleRepo roleRepo;

   /* private ResponseEntity<?> userExistsResponse (User entity){
        Optional<User> user = userService.getUSerByPseudo(entity.getPseudo());
        if(user.isPresent())
            return new ResponseEntity<>(
                    "pseudo existe déjà",
                    HttpStatus.CONFLICT
            );
        return null;
    }

    @PostMapping("register")
    public ResponseEntity<?> profRegister(@RequestBody User entity){
        ResponseEntity<?> res = userExistResponse(entity);
        if (res != null)
            return res;
        Optional<Role> role = roleRepo.findByRoleName(Role.RoleEnum.name());
        if(role.isEmpty())
            return new ResponseEntity<>(
                    "Une erreur est servenue !",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        return new ResponseEntity<>(
                authService.register(entity, role.get()),
                HttpStatus.CREATED
        );
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request){
        String pseudo = request.get("pseudo");
        String password = request.get("password");
        Optional<User> user = userService.getUSerByPseudo(pseudo);
        if (user.isEmpty())
            return new ResponseEntity(
                    "User n'existe pas",
                    HttpStatus.NOT_FOUND
            );
        String jwt = authService.login(user.get(), password);
        if (jwt == null)
            return new ResponseEntity<>(
                    "Mot de passe incorrect",
                    HttpStatus.FORBIDDEN
            );
        return new ResponseEntity<>(
                jwt,
                HttpStatus.OK
        );
    }*/
}
