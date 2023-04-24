package com.ll.social.app.member.service;

import com.ll.social.app.member.entity.Member;
import com.ll.social.app.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

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
}
