package com.moren.yohan.demo.repositories;

import com.moren.yohan.demo.models.Article;
import com.moren.yohan.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepo extends JpaRepository<Article, Long> {


    void delete(Article article);

    Optional<Article> findArticleById(Long id);

    Article save(Article article);
}
