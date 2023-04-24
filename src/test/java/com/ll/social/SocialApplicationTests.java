package com.ll.social;

import com.ll.social.app.home.controller.HomeController;
import com.ll.social.app.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
	@Rollback(value = false)
	void t2() throws Exception {
		long count = memberService.count();
		assertThat(count).isGreaterThanOrEqualTo(5L);
	}
}
