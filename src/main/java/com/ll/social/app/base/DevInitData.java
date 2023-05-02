package com.ll.social.app.base;

import com.ll.social.app.article.entity.Article;
import com.ll.social.app.article.service.ArticleService;
import com.ll.social.app.member.entity.Member;
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
    CommandLineRunner init(MemberService memberService, ArticleService articleService, PasswordEncoder passwordEncoder) {
        return args -> {
            Member member1 = memberService.join("user1", passwordEncoder.encode("1234"), "user1@naver.com");
            memberService.setProfileImgByUrl(member1, "https://mblogthumb-phinf.pstatic.net/20151115_83/owlkw_1447523205207J3E59_JPEG/Touch_%28Series%29_full_958683.jpg?type=w2");

            Member member2 = memberService.join("user2", passwordEncoder.encode("1234"), "user2@naver.com");
            memberService.setProfileImgByUrl(member2, "https://mblogthumb-phinf.pstatic.net/20151115_83/owlkw_1447523205207J3E59_JPEG/Touch_%28Series%29_full_958683.jpg?type=w2");

            Article article1 = articleService.write(member1, "제목1", "내용1", "#자바 #프로그래밍");
            articleService.addGenFileByUrl(article1, "common", "inBody", 1, "https://mblogthumb-phinf.pstatic.net/20151115_83/owlkw_1447523205207J3E59_JPEG/Touch_%28Series%29_full_958683.jpg?type=w2");
            articleService.addGenFileByUrl(article1, "common", "inBody", 2, "https://pbs.twimg.com/media/CvkzoakWEAA7nCj.jpg");
            articleService.addGenFileByUrl(article1, "common", "inBody", 3, "https://mblogthumb-phinf.pstatic.net/20151115_83/owlkw_1447523205207J3E59_JPEG/Touch_%28Series%29_full_958683.jpg?type=w2");
            articleService.addGenFileByUrl(article1, "common", "inBody", 4, "https://pbs.twimg.com/media/CvkzoakWEAA7nCj.jpg");

            Article article2 = articleService.write(member2, "제목2", "내용2", "#HTML #프로그래밍");
            articleService.addGenFileByUrl(article2, "common", "inBody", 1, "https://mblogthumb-phinf.pstatic.net/20151115_83/owlkw_1447523205207J3E59_JPEG/Touch_%28Series%29_full_958683.jpg?type=w2");
            articleService.addGenFileByUrl(article2, "common", "inBody", 2, "https://pbs.twimg.com/media/CvkzoakWEAA7nCj.jpg");

        };
    }
}
