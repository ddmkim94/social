package com.ll.social.app.article.controller;

import com.ll.social.app.article.entity.Article;
import com.ll.social.app.article.entity.dto.ArticleForm;
import com.ll.social.app.article.service.ArticleService;
import com.ll.social.app.fileupload.service.GenFileService;
import com.ll.social.app.security.dto.MemberContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final GenFileService genFileService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String writeForm() {
        // 타임리프 디폴트 prefix = templates/
        return "article/write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    @ResponseBody
    public String write(@AuthenticationPrincipal MemberContext memberContext, @Valid ArticleForm form, MultipartRequest multipartRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "article/write";
        }

        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        log.debug("fileMap : {}", fileMap);

        Article article = articleService.write(memberContext.getId(), form.getSubject(), form.getContent());
        genFileService.saveFile(article, fileMap);

        return "작업중";
    }
}
