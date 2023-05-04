package com.ll.social.app.article.service;

import com.ll.social.app.article.entity.Article;
import com.ll.social.app.article.repository.ArticleRepository;
import com.ll.social.app.gen.entity.GenFile;
import com.ll.social.app.gen.service.GenFileService;
import com.ll.social.app.hashtag.entity.HashTag;
import com.ll.social.app.hashtag.service.HashTagService;
import com.ll.social.app.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Slf4j
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

    public Article write(Long authorId, String subject, String content, String hashTagContent) {
        return write(new Member(authorId), subject, content, hashTagContent);
    }

    public Article write(Member author, String subject, String content) {
        return write(author, subject, content, "");
    }

    public Article write(Member author, String subject, String content, String hashTagContent) {
        Article article = Article.builder()
                .member(author)
                .subject(subject)
                .content(content)
                .build();

        articleRepository.save(article);
        hashTagService.applyHashTags(article, hashTagContent);

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

        loadForPrintData(article);

        return article;
    }

    public void loadForPrintData(Article article) {
        Map<String, GenFile> genFileMap = genFileService.getRelGenFileMap(article);
        List<HashTag> hashTags = hashTagService.getHashTags(article);

        article.getExtra().put("hashTags", hashTags);
        article.getExtra().put("genFileMap", genFileMap);
    }

    public void loadForPrintData(List<Article> articles) {
        long[] ids = articles
                .stream()
                .mapToLong(Article::getId)
                .toArray();

        List<HashTag> hashTagsByArticleIds = hashTagService.getHashTagsByArticleIdIn(ids);

        Map<Long, List<HashTag>> hashTagsByArticleIdsMap = hashTagsByArticleIds.stream()
                .collect(groupingBy(
                        hashTag -> hashTag.getArticle().getId(), toList()
                ));

        articles.stream().forEach(article -> {
            List<HashTag> hashTags = hashTagsByArticleIdsMap.get(article.getId());

            if (hashTags == null || hashTags.size() == 0) return;

            article.getExtra().put("hashTags", hashTags);
        });

        List<GenFile> genFilesByRelIdIn = genFileService.getRelGenFilesByRelIdIn("article", ids);

        Map<Long, List<GenFile>> genFilesMap = genFilesByRelIdIn
                .stream()
                .collect(groupingBy(
                        GenFile::getRelId, toList()
                ));

        articles.stream().forEach(article -> {
            List<GenFile> genFiles = genFilesMap.get(article.getId());

            if (genFiles == null || genFiles.size() == 0) return;

            article.getExtra().put("genFileMap", genFileService.getRelGenFileMap(genFiles));
        });

        log.debug("articles : " + articles);
    }

    public void modify(Article article, String subject, String content, String hashTagContent) {
        article.setSubject(subject);
        article.setContent(content);

        articleRepository.save(article);
        hashTagService.applyHashTags(article, hashTagContent);
    }

    public List<Article> getArticles() {
        return articleRepository.getQslArticles();
    }

    public List<Article> search(String kwType, String kw) {
        return articleRepository.searchQsl(kwType, kw);
    }
}
