package com.ll.social.app.security.dto;

import com.ll.social.app.member.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class MemberContext extends User {

    private final Long id;
    private final String profileImgUrl;

    public MemberContext(Member member, List<GrantedAuthority> authorityList) {
        super(member.getUsername(), member.getPassword(), authorityList);
        this.id = member.getId();
        this.profileImgUrl = member.getProfileImgUrl();
    }
}
