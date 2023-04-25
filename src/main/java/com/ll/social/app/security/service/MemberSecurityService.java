package com.ll.social.app.security.service;

import com.ll.social.app.member.entity.Member;
import com.ll.social.app.member.repository.MemberRepository;
import com.ll.social.app.security.dto.MemberContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberSecurityService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * 스프링 시큐리티 사용을 위해서 반드시 있어야함
     * username 줄테니까 사용자 정보 줘!
     * User -> username, password, 권한들로 이루어져있다.
     *
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByUsername(username).orElse(null);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("member"));

        return new MemberContext(member, authorities);
    }
}
