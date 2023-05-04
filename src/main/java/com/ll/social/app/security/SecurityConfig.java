package com.ll.social.app.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // @PreAuthorize() 적용
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2UserService oAuth2UserService;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(
                        csrf -> csrf.disable()
                )
                .authorizeRequests(
                        authz -> authz
                                .antMatchers("/**")
                                .permitAll()
                )
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/member/login") // GET
                                .loginProcessingUrl("/member/login") // POST
                                .successHandler(authenticationSuccessHandler)
                )
                .oauth2Login(oauth2Login -> oauth2Login // OAuth2 로그인 활성화
                        .loginPage("/member/login")
                        .successHandler(authenticationSuccessHandler)
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                .userService(oAuth2UserService))
                )
                .logout(logout -> logout
                        .logoutUrl("/member/logout"))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
