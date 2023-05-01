package com.ll.social.app.gen.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Controller
@RequestMapping("/upload")
@RequiredArgsConstructor
public class FileUploadController {

    @Value("${custom.genFileDirPath}")
    private String fileDirPath;

    @PostMapping
    @ResponseBody
    public String upload(
            @RequestParam("img1") MultipartFile img1,
            @RequestParam("img2") MultipartFile img2
    ) {
        try {
            img1.transferTo(new File(fileDirPath + img1.getOriginalFilename()));
            img2.transferTo(new File(fileDirPath + img2.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "업로드 완료!";
    }
}
