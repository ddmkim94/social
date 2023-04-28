package com.ll.social.app.fileupload.service;

import com.ll.social.app.article.entity.Article;
import com.ll.social.app.fileupload.entity.GenFile;
import com.ll.social.app.fileupload.repository.GenFileRepository;
import com.ll.social.app.util.Util;
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

            String[] inputNameBits = key.split("__");

            String originFileName = multipartFile.getOriginalFilename();
            String ext = Util.file.getExt(originFileName);
            String typeCode = inputNameBits[0];
            String type2Code = inputNameBits[1];
            String fileExt = ext;
            String fileExtTypeCode = Util.file.getFileExtTypeCodeFromFileExt(fileExt);
            String fileExtType2Code = Util.file.getFileExtType2CodeFromFileExt(fileExt);
            int fileNo = Integer.parseInt(inputNameBits[2]);
            int fileSize = (int) multipartFile.getSize();
            String fileDir = relTypeCode  +"/" + Util.date.getCurrentDateFormatted("yyyy_MM_dd");

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
