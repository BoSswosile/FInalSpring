package com.moren.yohan.demo.impl;

import com.moren.yohan.demo.models.Article;
import com.moren.yohan.demo.models.User;
import com.moren.yohan.demo.repositories.ArticleRepo;
import com.moren.yohan.demo.services.ArticleService;
import com.moren.yohan.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleImpl implements ArticleService {

    @Autowired
    ArticleRepo articleRepo;

    @Autowired
    UserService userService;

    @Override
    public List<Article> getAllArticles() {
        List<Article> articles = articleRepo.findAll();
        if (articles.isEmpty()) {
            return new ArrayList<>();
        }
        return articles;
    }

    @Override
    public Optional<Article> getArticleById(Long id) {
        return articleRepo.findById(id);
    }

    @Override
    public Article save(Article article) {
        return articleRepo.save(article);
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void deleteArticle(Article article) {
        articleRepo.delete(article);
    }

    @Override
    public Article addToStock(String email, Article article) {
        Optional<User> user = userService.findByEmail(email);
        articleRepo.save(article);
        user.get().getStock().add(article);
        userService.updateUser(user.get());
        return article;
    }
}
