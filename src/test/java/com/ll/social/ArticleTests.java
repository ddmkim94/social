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
    void test() {
        Article article = articleService.getArticleById(1L);
        List<HashTag> hashTagList = hashTagService.getHashTags(article);

        assertThat(hashTagList.size()).isEqualTo(2);
    }
}
