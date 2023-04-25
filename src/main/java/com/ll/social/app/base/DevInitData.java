package com.ll.social.app.base;

import com.ll.social.app.member.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("dev")
public class DevInitData {

    // CommandLineRunner : 앱 실행 직후 초기데이터 세팅 및 초기화에 사용한다.
    @Bean
    CommandLineRunner init(MemberService memberService, PasswordEncoder passwordEncoder) {
        return args -> {
            memberService.join("user1", passwordEncoder.encode("1234"), "user1@naver.com");
            memberService.join("user2", passwordEncoder.encode("1234"), "user2@naver.com");
            memberService.join("user3", passwordEncoder.encode("1234"), "user3@naver.com");
            memberService.join("user4", passwordEncoder.encode("1234"), "user4@naver.com");
            memberService.join("user5", passwordEncoder.encode("1234"), "user5@naver.com");
        };
    }
}
