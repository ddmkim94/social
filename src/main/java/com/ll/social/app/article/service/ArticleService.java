package com.ll.social.app.article.service;

import com.ll.social.app.article.entity.Article;
import com.ll.social.app.article.repository.ArticleRepository;
import com.ll.social.app.gen.entity.GenFile;
import com.ll.social.app.gen.service.GenFileService;
import com.ll.social.app.hashtag.entity.HashTag;
import com.ll.social.app.hashtag.service.HashTagService;
import com.ll.social.app.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final GenFileService genFileService;
    private final HashTagService hashTagService;

    public Article write(Long authorId, String subject, String content) {
        return write(new Member(authorId), subject, content);
    }

    public Article write(Member author, String subject, String content) {
        return write(author, subject, content, "");
    }

    public Article write(Member author, String subject, String content, String hashTags) {
        Article article = Article.builder()
                .member(author)
                .subject(subject)
                .content(content)
                .build();

        articleRepository.save(article);
        hashTagService.applyHashTags(article, hashTags);

        return article;
    }

    @Transactional(readOnly = true)
    public Article getArticleById(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    public void addGenFileByUrl(Article article, String typeCode, String type2Code, int fileNo, String url) {
        genFileService.addGenFileByUrl("article", article.getId(), typeCode, type2Code, fileNo, url);
    }

    @Transactional(readOnly = true)
    public Article getForPrintArticleById(Long id) {
        Article article = getArticleById(id);

        Map<String, GenFile> genFileMap = genFileService.getRelGenFileMap(article);
        List<HashTag> hashTags = hashTagService.getHashTags(article);

        article.getExtra().put("hashTags", hashTags);
        article.getExtra().put("genFileMap", genFileMap);

        return article;
    }

    public void modify(Article article, String subject, String content) {
        article.setSubject(subject);
        article.setContent(content);

        articleRepository.save(article);
    }
}
