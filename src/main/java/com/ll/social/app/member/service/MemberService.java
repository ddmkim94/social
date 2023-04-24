package com.ll.social.app.member.service;

import com.ll.social.app.member.entity.Member;
import com.ll.social.app.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Value("${custom.genFileDirPath}")
    private String fileDirPath;

    public void join(String username, String password, String email, MultipartFile profileImg) {
        String profileImgPath = fileDirPath + profileImg.getOriginalFilename();
        try {
            profileImg.transferTo(new File(profileImgPath));

            Member member = Member.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .profileImg("http://localhost:8010/gen/" + profileImg.getOriginalFilename())
                    .build();

            memberRepository.save(member);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(readOnly = true)
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username).get();
    }
}
