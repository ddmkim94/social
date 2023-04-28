package com.ll.social.app.fileupload.service;

import com.ll.social.app.article.entity.Article;
import com.ll.social.app.fileupload.entity.GenFile;
import com.ll.social.app.fileupload.repository.GenFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GenFileService {

    private final GenFileRepository genFileRepository;

    public void saveFile(Article article, Map<String, MultipartFile> fileMap) {

        String relTypeCode = "article";
        long relId = article.getId();

        for (String key : fileMap.keySet()) {
            MultipartFile multipartFile = fileMap.get(key);

            String typeCode = "common";
            String type2Code = "inBodyImg";
            String fileExt = "jpg";
            String fileExtTypeCode = "img";
            String fileExtType2Code = "jpg";
            int fileNo = 1;
            int fileSize = 1000;
            String fileDir = "article/2023_04_28";
            String originFileName = "??";

            GenFile genFile = GenFile.builder()
                    .relTypeCode(relTypeCode)
                    .relId(relId)
                    .typeCode(typeCode)
                    .type2Code(type2Code)
                    .fileExtTypeCode(fileExtTypeCode)
                    .fileExtType2Code(fileExtType2Code)
                    .fileNo(fileNo)
                    .fileSize(fileSize)
                    .fileDir(fileDir)
                    .fileExt(fileExt)
                    .originFileName(originFileName)
                    .build();

            genFileRepository.save(genFile);
        }
    }
}
