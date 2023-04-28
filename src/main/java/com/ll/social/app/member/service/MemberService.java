package com.ll.social.app.member.service;

import com.ll.social.app.member.entity.Member;
import com.ll.social.app.member.repository.MemberRepository;
import com.ll.social.app.util.Util;
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

    private String getCurrentProfileImgDirName() {
        return "member/" + Util.date.getCurrentDateFormatted("yyyy_MM_dd");
    }



    public Member join(String username, String password, String email, MultipartFile profileImg) {
        String profileImgRelPath = saveProfileImg(profileImg);

        Member member = Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .profileImg(profileImgRelPath)
                .build();

        memberRepository.save(member);

        return member;
    }

    private String saveProfileImg(MultipartFile profileImg) {
        if (profileImg == null || profileImg.isEmpty()) {
            return null;
        }

        String profileImgDirName = getCurrentProfileImgDirName();

        String ext = Util.file.getExt(profileImg.getOriginalFilename());

        String fileName = UUID.randomUUID().toString() + "." + ext;
        String profileImgDirPath = fileDirPath + profileImgDirName; // /Users/dongmin/Desktop/upload/member
        String profileImgFilePath = profileImgDirPath + "/" + fileName; // /Users/dongmin/Desktop/upload/member/touch.png

        new File(profileImgDirPath).mkdirs(); // 폴더가 없는 경우 해당 폴더를 생성해준다.

        try {
            profileImg.transferTo(new File(profileImgFilePath));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // DB에 저장할 파일의 경로
        return profileImgDirName + "/" + fileName;
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

    public long count() {
        return memberRepository.count();
    }

    public void removeProfileImg(Member member) {
        member.removeProfileImgOnStorage();
        member.setProfileImg(null);

        memberRepository.save(member);
    }

    public void modify(Member member, String email, MultipartFile profileImg) {
        removeProfileImg(member); // 기존 프로필 사진 삭제
        String profileImgPath = saveProfileImg(profileImg);

        member.setEmail(email);
        member.setProfileImg(profileImgPath);

        memberRepository.save(member);
    }

    public void setProfileImgByUrl(Member member, String url) {
        String filePath = Util.file.downloadImg(url, fileDirPath + getCurrentProfileImgDirName() + "/" + UUID.randomUUID());
        member.setProfileImg(getCurrentProfileImgDirName() + "/" + new File(filePath).getName());
        memberRepository.save(member);
    }
}
