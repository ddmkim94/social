package com.ll.social.app.member.controller;

import com.ll.social.app.member.entity.Member;
import com.ll.social.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/join")
    public String joinForm() {
        return "member/join";
    }

    @PostMapping("/join")
    @ResponseBody
    public String join(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam("profileImg") MultipartFile profileImg) {

        Member oldMember = memberService.getMemberByUsername(username);

        if (oldMember != null) {
            return "이미 가입된 회원입니다!";
        }

        // noop: 비번 암호화 시키고 싶을 때 붙여줌.
        // TODO: PasswordEncoder 가 대체할 예정
        memberService.join(username, "{noop}" + password, email, profileImg);

        return "가입완료!";
    }

    @GetMapping("/profile")
    public String profile(Model model, @RequestParam("username") String username) {
        model.addAttribute("member", memberService.getMemberByUsername(username));
        return "member/profile";
    }
}
