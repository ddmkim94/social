package com.ll.social.app.article.repository;

import com.ll.social.app.article.entity.Article;
import com.ll.social.app.article.entity.QArticle;
import com.ll.social.util.Util;
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

    @Override
    public List<Article> searchQsl(String kwType, String kw) {
        if (Util.str.eq(kwType, "hashTag") && Util.str.empty(kw)) {
            // 키워드 검색
            return jpaQueryFactory
                    .selectFrom(article)
                    .orderBy(article.id.desc())
                    .fetch();
        }

        // 키워드가 없거나 검색 타입이 해시태그가 아닌 경우, 전체 게시물 리턴?
        return jpaQueryFactory
                .selectFrom(article)
                .orderBy(article.id.desc())
                .fetch();
    }
}
