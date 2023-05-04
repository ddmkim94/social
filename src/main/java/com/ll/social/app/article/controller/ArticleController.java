package com.ll.social.app.article.controller;

import com.ll.social.app.article.entity.Article;
import com.ll.social.app.article.entity.dto.ArticleForm;
import com.ll.social.app.article.service.ArticleService;
import com.ll.social.app.base.dto.RsData;
import com.ll.social.app.gen.entity.GenFile;
import com.ll.social.app.gen.service.GenFileService;
import com.ll.social.app.security.dto.MemberContext;
import com.ll.social.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final GenFileService genFileService;

    @GetMapping("/list")
    public String list(Model model) {
        List<Article> articles = articleService.getArticles();

        articleService.loadForPrintData(articles);

        model.addAttribute("articles", articles);
        return "article/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String writeForm() {
        // 타임리프 디폴트 prefix = templates/
        return "article/write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String write(
            @AuthenticationPrincipal MemberContext memberContext,
            @Valid ArticleForm form,
            MultipartRequest multipartRequest,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "article/write";
        }

        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        log.debug("fileMap : {}", fileMap);

        Article article = articleService.write(memberContext.getId(), form.getSubject(), form.getContent(), form.getHashTagContent());

        RsData<Map<String, GenFile>> saveFileRsData = genFileService.saveFile(article, fileMap);
        log.debug("saveFileRsDate : {}", saveFileRsData);

        String msg = "%d번 게시물이 작성되었습니다.".formatted(article.getId());
        msg = Util.url.encode(msg);
        return "redirect:/article/%d?msg=%s".formatted(article.getId(), msg);
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Article article = articleService.getForPrintArticleById(id);
        model.addAttribute("article", article);

        return "article/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/modify")
    public String modifyForm(@AuthenticationPrincipal MemberContext memberContext, @PathVariable Long id, Model model) {
        Article article = articleService.getForPrintArticleById(id);

        if (memberContext.getId() != article.getMember().getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); // 403
        }

        model.addAttribute("article", article);

        return "article/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/modify")
    public String modify(
            @AuthenticationPrincipal MemberContext memberContext,
            @PathVariable Long id,
            @Valid ArticleForm articleForm,
            MultipartRequest multipartRequest,
            @RequestParam Map<String, String> params // @RequestParam + Map 으로 받으면 모든 필드를 전부 받아옴
    ) {
        Article article = articleService.getForPrintArticleById(id);

        if (memberContext.getId() != article.getMember().getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); // 403
        }

        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

        genFileService.deleteFiles(article, params);
        RsData<Map<String, GenFile>> saveFileRsData = genFileService.saveFile(article, fileMap);

        articleService.modify(article, articleForm.getSubject(), articleForm.getContent(), articleForm.getHashTagContent());

        String msg = Util.url.encode("%d번 게시물이 수정되었습니다.".formatted(id));
        return "redirect:/article/%d?msg=%s".formatted(id, msg);
    }
}
