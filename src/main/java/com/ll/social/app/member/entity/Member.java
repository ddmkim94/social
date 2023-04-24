package com.ll.social.app.member.entity;

import com.ll.social.app.base.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {

    private String username;
    private String password;
    private String email;
    private String profileImg;
}
