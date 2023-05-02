package com.ll.social.app.base;

import com.ll.social.app.article.service.ArticleService;
import com.ll.social.app.member.entity.Member;
import com.ll.social.app.member.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("test")
public class TestInitData {

    // CommandLineRunner : 앱 실행 직후 초기데이터 세팅 및 초기화에 사용한다.
    @Bean
    CommandLineRunner init(MemberService memberService, ArticleService articleService, PasswordEncoder passwordEncoder) {
        return args -> {
            String password = passwordEncoder.encode("1234");
            Member member1 = memberService.join("user1", password, "user1@naver.com");
            Member member2 = memberService.join("user2", password, "user2@naver.com");
            Member member3 =memberService.join("user3", password, "user3@naver.com");
            Member member4 =memberService.join("user4", password, "user4@naver.com");
            Member member5 =memberService.join("user5", password, "user5@naver.com");

            articleService.write(member1, "title1", "content1", "#자바 #프로그래밍");
            articleService.write(member1, "title2", "content2", "#박은빈 #자바");
        };
    }
}
