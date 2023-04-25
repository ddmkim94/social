package com.ll.social.app.member.controller;

import com.ll.social.app.member.entity.Member;
import com.ll.social.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String joinForm() {
        return "member/join";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String LoginForm() {
        return "member/login";
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

    /**
     * 로그인이 필요한 메서드
     * 비로그인 상태에서 호출하면 로그인 페이지로 이동한다.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {

        Member loginedMember = memberService.getMemberByUsername(principal.getName());

        model.addAttribute("member", loginedMember);

        return "member/profile";
    }
}
