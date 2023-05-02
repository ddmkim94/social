package com.ll.social.app.article.repository;

import com.ll.social.app.article.entity.Article;
import com.ll.social.app.article.entity.QArticle;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ll.social.app.article.entity.QArticle.article;

@RequiredArgsConstructor
public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Article> getQslArticles() {
        return jpaQueryFactory
                .selectFrom(article)
                .orderBy(article.id.desc())
                .fetch();
    }
}
