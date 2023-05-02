package com.ll.social;

import com.ll.social.app.article.entity.Article;
import com.ll.social.app.article.service.ArticleService;
import com.ll.social.app.hashtag.entity.HashTag;
import com.ll.social.app.hashtag.service.HashTagService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class ArticleTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private HashTagService hashTagService;


    @Test
    @DisplayName("1번 게시물에는 키워드가 2개 존재한다.")
    void t1() {
        Article article = articleService.getArticleById(1L);
        List<HashTag> hashTagList = hashTagService.getHashTags(article);

        assertThat(hashTagList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("1번 게시물의 해시태그를 수정하면, 기존 해시태그 중 몇개는 지워질 수 있다.")
    void t2() {
        String keywordContent = "#자바 #개발";
        Article article = articleService.getArticleById(1L);
        hashTagService.applyHashTags(article, keywordContent);
    }

    @Test
    @DisplayName("자바 해시태그와 관련된 모든 게시물 조회")
    void t3() {
        List<Article> articles = articleService.search("hashtag", "자바");

        assertThat(articles.size()).isEqualTo(2);
    }
}
