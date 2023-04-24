package com.ll.social.app.member.controller;

import com.ll.social.app.member.entity.Member;
import com.ll.social.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

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
            @RequestParam("profileImg") MultipartFile profileImg,
            HttpSession session) {

        Member oldMember = memberService.getMemberByUsername(username);

        if (oldMember != null) {
            return "redirect:/?errorMsg=Already Join User!";
        }

        // noop: 비번 암호화 시키고 싶을 때 붙여줌.
        // TODO: PasswordEncoder 가 대체할 예정
        Member member = memberService.join(username, "{noop}" + password, email, profileImg);

        session.setAttribute("loginedMemberId", member.getId());

        return "redirect:/member/profile";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        Long loginedMemberId = (Long) session.getAttribute("loginedMemberId");
        boolean isLogined = loginedMemberId != null;

        if (!isLogined) {
            // TODO: 한글 처리 필요함
            return "redirect:/?errorMsg=Need to Login!";
        }

        Member loginedMember = memberService.getMemberById(loginedMemberId);
        model.addAttribute("member", loginedMember);

        return "member/profile";
    }
}
