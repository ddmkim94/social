package com.ll.social.app.article.entity.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class ArticleForm {

    @NotEmpty(message = "제목은 필수 입력값입니다.")
    private String subject;

    @NotEmpty(message = "내용은 필수 입력값입니다.")
    private String content;

    private String hashTagContent;

}
