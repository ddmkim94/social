package com.ll.social.app.base;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    // static 변수에는 @Value 사용 불가능
    public static String FILE_DIR_PATH;

    @Value("${custom.genFileDirPath}")
    public void setFileDirPath(String fileDirPath) {
        FILE_DIR_PATH = fileDirPath;
    }
}
