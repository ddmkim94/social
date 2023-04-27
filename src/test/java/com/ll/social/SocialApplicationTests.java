package com.ll.social;

import com.ll.social.app.home.controller.HomeController;
import com.ll.social.app.member.controller.MemberController;
import com.ll.social.app.member.entity.Member;
import com.ll.social.app.member.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles({"base-addi", "test"})
class SocialApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private MemberService memberService;

	@Test
	@DisplayName("메인 화면에는 메인이라는 글자가 포함되어있다.")
	void t1() throws Exception {
		// WHEN
		ResultActions resultActions = mvc
				.perform(get("/"))
				.andDo(print());// 요청을 했을 때 응답과 관련된 내용을 콘솔에 출력해라!

		// THEN
		resultActions
				.andExpect(status().is2xxSuccessful())
				.andExpect(handler().handlerType(HomeController.class))
				.andExpect(handler().methodName("main"))
				.andExpect(content().string(containsString("메인")))
				.andExpect(content().contentType("text/html;charset=UTF-8"))
				.andExpect(view().name("home/main"));
	}

	@Test
	@DisplayName("회원의 수")
	void t2() throws Exception {
		long count = memberService.count();
		assertThat(count).isGreaterThanOrEqualTo(5L);
	}

	@Test
	@DisplayName("user1로 로그인 후 프로필 페이지에 접속하면 user1의 이메일이 보여야한다.")
	@WithUserDetails("user1")
	void t3() throws Exception {

		ResultActions resultActions = mvc.perform(get("/member/profile")).andDo(print());

		resultActions
				.andExpect(status().is2xxSuccessful())
				.andExpect(handler().handlerType(MemberController.class))
				.andExpect(handler().methodName("profile"))
				.andExpect(content().string(containsString("user1@naver.com")));
	}

	@Test
	@DisplayName("user4로 로그인 후 프로필 페이지에 접속하면 user4의 이메일이 보여야한다.")
	@WithUserDetails("user4")
	void t4() throws Exception {
		ResultActions resultActions = mvc.perform(get("/member/profile")).andDo(print());

		resultActions
				.andExpect(status().is2xxSuccessful())
				.andExpect(handler().handlerType(MemberController.class))
				.andExpect(handler().methodName("profile"))
				.andExpect(content().string(containsString("user4@naver.com")));

	}

	@Test
	@DisplayName("회원가입")
	@Rollback(false)
	void t5() throws Exception {

		String testUploadFileUrl = "https://mblogthumb-phinf.pstatic.net/20151115_83/owlkw_1447523205207J3E59_JPEG/Touch_%28Series%29_full_958683.jpg?type=w2";
		String originalFileName = "touch.png";

		// 파일 다운로드
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Resource> response = restTemplate.getForEntity(testUploadFileUrl, Resource.class);
		InputStream inputStream = response.getBody().getInputStream();

		MockMultipartFile profileImg = new MockMultipartFile(
				"profileImg",
				originalFileName,
				"image/png",
				inputStream);

		// 회원가입
		mvc.perform(multipart("/member/join")
						.file(profileImg)
						.param("username", "eunbin")
						.param("password", "1234")
						.param("email", "eunbin@naver.com")
						.characterEncoding("UTF-8")
				)
				.andExpect(status().is3xxRedirection())
				.andExpect(handler().handlerType(MemberController.class))
				.andExpect(handler().methodName("join"))
				.andExpect(redirectedUrl("/member/profile"))
				.andDo(print());

		Member member = memberService.getMemberById(6L);
		assertThat(member.getUsername()).isEqualTo("eunbin");
		assertThat(member.getEmail()).isEqualTo("eunbin@naver.com");

		// 업로드 폴더에 저장된 프로필 사진 제거
		memberService.removeProfileImg(member);

	}
}