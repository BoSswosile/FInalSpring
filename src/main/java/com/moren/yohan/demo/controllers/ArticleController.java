package com.moren.yohan.demo.controllers;

import com.moren.yohan.demo.models.Article;
import com.moren.yohan.demo.models.User;
import com.moren.yohan.demo.security.JwtService;
import com.moren.yohan.demo.services.ArticleService;
import com.moren.yohan.demo.services.UserService;
import io.jsonwebtoken.Jwt;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @Autowired
    JwtService jwtService;

    @Autowired
    UserService userService;

    @GetMapping("")
    @Operation(summary = "Get all articles", description = "Get all articles")
    public ResponseEntity<?> getAllArticles() {
        if (articleService.getAllArticles().isEmpty()) {
            return new ResponseEntity<>("No articles found", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(articleService.getAllArticles(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get article by id", description = "Get article by id")
    public ResponseEntity<?> getArticleById(@PathVariable Long id) {
        if (articleService.getArticleById(id).isEmpty()) {
            return new ResponseEntity<>("Article not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(articleService.getArticleById(id), HttpStatus.OK);
    }

    @PostMapping("")
    @Operation(summary = "Create article", description = "Create a new article")
    public ResponseEntity<?> createArticle(@RequestHeader("Authorization") String jwt, @Valid @RequestBody Article article, BindingResult result) {
        if (jwt == null) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        jwt = jwt.replace("Bearer ", "");
        jwtService.extractUsername(jwt);
        String email = jwtService.extractUsername(jwt);
        if (article == null) {
            return new ResponseEntity<>("Article not created", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (result.hasErrors()) {
            return new ResponseEntity<>("Invalid data", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(articleService.addToStock(email, article), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @Operation(summary = "Update article", description = "Update an article")
    public ResponseEntity<?> updateArticle(@RequestHeader("Authorization") String jwt, @PathVariable Long id, @RequestBody Article article) {
        if (jwt == null) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        jwt = jwt.replace("Bearer ", "");
        jwtService.extractUsername(jwt);
        String email = jwtService.extractUsername(jwt);
        Optional<User> user = userService.findByEmail(email);
        if (user.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        if (articleService.getArticleById(id).isEmpty()) {
            return new ResponseEntity<>("Article not found", HttpStatus.NOT_FOUND);
        }
        for (Article a : user.get().getStock()) {
            if (a.getId().equals(id)) {
                article.setId(id);
                return new ResponseEntity<>(articleService.save(article), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Article not found in stock", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete article", description = "Delete an article")
    public ResponseEntity<?> deleteArticle(@RequestHeader("Authorization") String jwt, @PathVariable Long id) {
        Article article = articleService.getArticleById(id).orElse(null);
        if (jwt == null) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        jwt = jwt.replace("Bearer ", "");
        jwtService.extractUsername(jwt);
        String email = jwtService.extractUsername(jwt);
        Optional<User> user = userService.findByEmail(email);
        if (user.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        if (articleService.getArticleById(id).isEmpty()) {
            return new ResponseEntity<>("Article not found", HttpStatus.NOT_FOUND);
        }
        for (Article a : user.get().getStock()) {
            if (a.getId().equals(id)) {
                articleService.deleteArticle(article);
                return new ResponseEntity<>("Article sucessfully deleted", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Article not found in stock", HttpStatus.NOT_FOUND);
    }
}

