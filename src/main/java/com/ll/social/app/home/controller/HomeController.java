package com.ll.social.app.home.controller;

import com.ll.social.app.member.entity.Member;
import com.ll.social.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;

    @GetMapping("/")
    public String main() {
        return "home/main";
    }

    @GetMapping("/about")
    public String about() {
        return "home/about";
    }
}
