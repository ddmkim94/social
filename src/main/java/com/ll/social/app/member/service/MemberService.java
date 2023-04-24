package com.ll.social.app.member.service;

import com.ll.social.app.member.entity.Member;
import com.ll.social.app.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    @Value("${custom.genFileDirPath}")
    private String fileDirPath;

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Member getMemberByUsername(String username) {
        return memberRepository.findByUsername(username).orElse(null);
    }

    @Transactional(readOnly = true)
    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    public Member join(String username, String password, String email, MultipartFile profileImg) {

        String profileImgRelPath = "member/" + UUID.randomUUID().toString() + ".png";

        File profileImgFile = new File(fileDirPath + profileImgRelPath);
        profileImgFile.mkdirs(); // 폴더가 없는 경우 해당 폴더를 생성해준다.

        try {
            profileImg.transferTo(profileImgFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Member member = Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .profileImg(profileImgRelPath)
                .build();

        memberRepository.save(member);

        return member;
    }

    public Member join(String username, String password, String email) {
        Member member = Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .build();

        memberRepository.save(member);

        return member;
    }

    /**
     * 스프링 시큐리티 사용을 위해서 반드시 있어야함
     * username 줄테니까 사용자 정보 줘!
     * User -> username, password, 권한들로 이루어져있다.
     *
      */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByUsername(username).orElse(null);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("member"));

        return new User(member.getUsername(), member.getPassword(), authorities);
    }

    public long count() {
        return memberRepository.count();
    }
}
