package com.ll.social.app.member.controller;

import com.ll.social.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String join(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam("profileImg") MultipartFile profileImg
    ) {

        memberService.join(username, password, email, profileImg);
        return "redirect:/member/profile?username=" + username;
    }

    @GetMapping("/profile")
    public String profile(Model model, @RequestParam("username") String username) {
        model.addAttribute("member", memberService.findByUsername(username));
        return "member/profile";
    }
}
