package com.ll.social.app.member.controller;

import com.ll.social.app.member.entity.Member;
import com.ll.social.app.member.service.MemberService;
import com.ll.social.app.security.dto.MemberContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String LoginForm() {
        return "member/login";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String joinForm() {
        return "member/join";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    public String join(
            HttpServletRequest req,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam("profileImg") MultipartFile profileImg) {

        Member oldMember = memberService.getMemberByUsername(username);

        String passwordClearText = password;
        password = passwordEncoder.encode(password);

        if (oldMember != null) {
            return "redirect:/?errorMsg=Already Join User!";
        }

        Member member = memberService.join(username, password, email, profileImg);

        try {
            // 패스워드 원문을 파라미터로 넘겨줘야한다.
            req.login(username, passwordClearText);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/member/profile";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify")
    public String modifyForm() {
        return "member/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify")
    public String modify(@AuthenticationPrincipal MemberContext context,
                         @RequestParam("email") String email,
                         @RequestParam("profileImg") MultipartFile profileImg,
                         @RequestParam(value = "profile__delete", required = false) String profileDelete) {

        Member member = memberService.getMemberById(context.getId());

        if (profileDelete != null && profileDelete.equals("Y")) {
            memberService.removeProfileImg(member);
        }

        memberService.modify(member, email, profileImg);

        // 기존에 시큐리티 세션에 저장된 MemberContext 객체의 내용을 수정
        context.setModifyDate(member.getModifyDate());

        // 스프링 시큐리티 세션 갱신
        /**
         * 세션에 값을 가지고 있으면 화면에서 간편하게 가져다 쓸 수 있는 장점이 있음.
         * 또 사용자 정보를 db에서 가져올 필요가 없기 때문에 쿼리 실행도 줄기 때문에 성능적 이점도 가짐.
         * 단, 세션과 db 사이의 데이터 불일치가 일어날 수 있기 때문에 데이터 동기화 작업을 신중하게 해줘야함..!
         * 이걸 "trade-off" 라고 함.
         */
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        context,
                        member.getPassword(),
                        context.getAuthorities()
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/member/profile";
    }

    /**
     * 로그인이 필요한 메서드
     * 비로그인 상태에서 호출하면 로그인 페이지로 이동한다.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String profile() {
        return "member/profile";
    }

    @GetMapping("/profile/img/{id}")
    public ResponseEntity<Object> profileImg(@PathVariable Long id) throws URISyntaxException {
        String profileImgUrl = memberService.getMemberById(id).getProfileImgUrl();

        if (profileImgUrl == null) {
            profileImgUrl = "https://via.placeholder.com/200x200.png?text=U_U";
        }

        URI redirectUri = new URI(profileImgUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(redirectUri);
        headers.setCacheControl(CacheControl.maxAge(60 * 60 * 1, TimeUnit.SECONDS)); // 캐시 유효기간 1시간
        return new ResponseEntity<>(headers, HttpStatus.FOUND);

    }
}
