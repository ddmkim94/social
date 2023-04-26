package com.ll.social.app.security.service;

import com.ll.social.app.member.entity.Member;
import com.ll.social.app.member.exception.MemberNotFoundException;
import com.ll.social.app.member.repository.MemberRepository;
import com.ll.social.app.member.service.MemberService;
import com.ll.social.app.security.dto.MemberContext;
import com.ll.social.app.security.exception.OAuthTypeMatchNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
                .getUserNameAttributeName();

        Map<String, Object> attributes = oAuth2User.getAttributes();

        String oauthId = oAuth2User.getName();

        Member member = null;
        String oauthType = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        if (!"KAKAO".equals(oauthType)) {
            throw new OAuthTypeMatchNotFoundException();
        }

        if (isNew(oauthType, oauthId)) {
            switch (oauthType) {
                case "KAKAO" -> {
                    log.debug("attributes : {}",attributes);

                    Map attributesProperties = (Map) attributes.get("properties");
                    Map attributesKakaoAccount = (Map) attributes.get("kakao_account");

                    String nickname = (String) attributesProperties.get("nickname");
                    String profileImage = (String) attributesProperties.get("profile_image");
                    String email = "%s@kakao.com".formatted(oauthId); // 이메일이 안넘어올수도 있기 때문에 임의로 만들어줌 (oauthId는 로그인 하면 넘어오는 id값)
                    String username = "KAKAO_%s".formatted(oauthId); // username도 임의로 만들어줌

                    if ((boolean) attributesKakaoAccount.get("has_email")) {
                        email = (String) attributesKakaoAccount.get("email");
                    }

                    member = Member.builder()
                            .email(email)
                            .username(username)
                            .password("") // 패스워드 따로 없음
                            .build();

                    memberRepository.save(member);
                    memberService.setProfileImgByUrl(member, profileImage);
                }
            }
        } else {
            member = memberRepository.findByUsername("%s_%s".formatted(oauthType, oauthId))
                    .orElseThrow(MemberNotFoundException::new);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("member"));
        return new MemberContext(member, authorities, attributes, userNameAttributeName);
    }

    // 최초 가입이라면 가입과 동시에 로그인!
    private boolean isNew(String oAuthType, String oAuthId) {
        return memberRepository.findByUsername("%s_%s".formatted(oAuthType, oAuthId)).isEmpty();
    }
}