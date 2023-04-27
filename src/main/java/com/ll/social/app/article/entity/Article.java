package com.ll.social.app.article.entity;

import com.ll.social.app.base.entity.BaseEntity;
import com.ll.social.app.fileupload.entity.GenFile;
import com.ll.social.app.member.entity.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Article extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String subject;
    private String content;


}
