package com.moren.yohan.demo.services;

import com.moren.yohan.demo.models.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleService {
    List<Article> getAllArticles();
    Optional<Article> getArticleById(Long id);
    Article save(Article article);
    void delete(Long id);

    void deleteArticle(Article article);

    Article addToStock(String email, Article article);
}
