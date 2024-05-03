package com.moren.yohan.demo.controllers;

import com.moren.yohan.demo.models.Article;
import com.moren.yohan.demo.models.User;
import com.moren.yohan.demo.requests.StockRequest;
import com.moren.yohan.demo.security.JwtService;
import com.moren.yohan.demo.services.ArticleService;
import com.moren.yohan.demo.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    @Autowired
    ArticleService articleService;

    @Autowired
    JwtService jwtService;

    @Autowired
    UserService userService;

    @PostMapping("entry")
    @Operation(summary = "Add stock entry", description = "Add a new stock entry", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StockRequest.class))))
    public ResponseEntity<?> addStockEntry(@RequestHeader("Authorization") String jwt, @RequestBody Map<String, String> entry) {
        Long productId = Long.parseLong(entry.get("productId"));
        if (jwt == null) {
            return ResponseEntity.badRequest().body("Unauthorized");
        }
        if (entry.isEmpty()) {
            return ResponseEntity.badRequest().body("Entry is empty");
        }
        jwt = jwt.replace("Bearer ", "");
        jwtService.extractUsername(jwt);
        String email = jwtService.extractUsername(jwt);
        Optional<User> user = userService.findByEmail(email);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        for (Article article : articleService.getAllArticles()) {
            if (article.getId().equals(productId)) {
                article.setQuantity(article.getQuantity() + Integer.parseInt(entry.get("quantity")));
                return ResponseEntity.ok(articleService.save(article));
            }
        }
        return ResponseEntity.badRequest().body("Product not found");
    }

    @PostMapping("exit")
    @Operation(summary = "Add stock exit", description = "Add a new stock exit", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = StockRequest.class))))
    public ResponseEntity<?> addStockExit(@RequestHeader("Authorization") String jwt, @RequestBody Map<String, String> entry) {
        Long productId = Long.parseLong(entry.get("productId"));
        if (jwt == null) {
            return ResponseEntity.badRequest().body("Unauthorized");
        }
        if (entry.isEmpty()) {
            return ResponseEntity.badRequest().body("Entry is empty");
        }
        jwt = jwt.replace("Bearer ", "");
        jwtService.extractUsername(jwt);
        String email = jwtService.extractUsername(jwt);
        Optional<User> user = userService.findByEmail(email);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        for (Article article : articleService.getAllArticles()) {
            if (article.getId().equals(productId)) {
                article.setQuantity(article.getQuantity() - Integer.parseInt(entry.get("quantity")));
                return ResponseEntity.ok(articleService.save(article));
            }
        }
        return ResponseEntity.badRequest().body("Product not found");
    }

    @GetMapping("inventory")
    @Operation(summary = "Get all stock", description = "Get all stock entries")
    public ResponseEntity<?> getAllStock(@RequestHeader("Authorization") String jwt) {
        if (jwt == null) {
            return ResponseEntity.badRequest().body("Unauthorized");
        }
        jwt = jwt.replace("Bearer ", "");
        jwtService.extractUsername(jwt);
        String email = jwtService.extractUsername(jwt);
        Optional<User> user = userService.findByEmail(email);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        return ResponseEntity.ok(articleService.getAllArticles());
    }
}

