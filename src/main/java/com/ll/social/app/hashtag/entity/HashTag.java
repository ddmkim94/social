package com.ll.social.app.hashtag.entity;

import com.ll.social.app.article.entity.Article;
import com.ll.social.app.base.entity.BaseEntity;
import com.ll.social.app.keyword.entity.Keyword;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true, exclude = {"article", "keyword"})
public class HashTag extends BaseEntity {

    @ManyToOne
    private Article article;

    @ManyToOne
    private Keyword keyword;
}
