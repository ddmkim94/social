package com.ll.social.app.article.service;

import com.ll.social.app.article.entity.Article;
import com.ll.social.app.article.repository.ArticleRepository;
import com.ll.social.app.member.entity.Member;
import com.ll.social.app.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Article write(Long authorId, String subject, String content) {
        Article article = Article.builder()
                .member(new Member(authorId))
                .subject(subject)
                .content(content)
                .build();

        return articleRepository.save(article);
    }

    public Article getArticleById(Long id) {
        return articleRepository.findById(id).orElse(null);
    }
}
