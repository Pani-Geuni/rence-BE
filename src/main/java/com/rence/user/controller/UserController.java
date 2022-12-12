/**
 * @author 강경석
 * 로그인,로그아웃 
 * 회원 탈퇴
 * 아이디, 비밀번호 찾기
 */
package com.rence.user.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rence.user.model.EmailVO;
import com.rence.user.model.UserDto;
import com.rence.user.service.UserSendEmail;
import com.rence.user.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(tags = "유저 컨트롤러")
@Slf4j
@RestController
@RequestMapping("/rence")
public class UserController {

	@Autowired
	UserService service;

	@Autowired
	HttpSession session;

	@Autowired
	UserSendEmail authSendEmail;

	// 자동 개행 및 줄 바꿈 (new Gson은 일자로 나옴)
	Gson gson = new GsonBuilder().setPrettyPrinting().create();

	// **********************
	// 로그인 완료
	// **********************
	@ApiOperation(value = "로그인 성공", notes = "로그인 성공 입니다")
	@PostMapping("/loginSuccess")
	public String user_loginOK(@RequestParam String username, HttpServletResponse response) {
		log.info("user_loginOK ()...");
		log.info("username: {}", username);
		
		Map<String, String> map = new HashMap<String, String>();

		// 로그인 성공시 기존의 유저관련쿠키 제거
		Cookie cc = new Cookie("user_no", null); // choiceCookieName(쿠키 이름)에 대한 값을 null로 지정
		Cookie cc2 = new Cookie("user_image", null);
		cc.setMaxAge(0); // 유효시간을 0으로 설정
		cc2.setMaxAge(0);
		response.addCookie(cc); // 응답 헤더에 추가해서 없어지도록 함
		response.addCookie(cc2);

		UserDto udto = service.user_login_info(username);

		

		session.setAttribute("user_id", udto.getUser_id());

		Cookie cookie = new Cookie("user_no", udto.getUser_no()); // 고유번호 쿠키 저장
		Cookie cookie2 = new Cookie("user_image", udto.getUser_image()); // 고유번호 쿠키 저장
		cookie.setPath("/");
		cookie2.setPath("/");
		response.addCookie(cookie);
		response.addCookie(cookie2);

		log.info("User Login success.....");
		map.put("result", "1"); // 로그인 성공

		String jsonObject = gson.toJson(map);

		return jsonObject;
	}

	// **********************
	// 로그인 실패
	// **********************
	@ApiOperation(value = "로그인 실패", notes = "로그인 실패 입니다")
	@PostMapping("/loginFail")
	@ResponseBody
	public String user_loginFail(UserDto udto, HttpServletResponse response) {
		log.info("user_loginFail ()...");
		log.info("result: {}", udto);

		Map<String, String> map = new HashMap<String, String>();

		log.info("User Login failed.....");
		map.put("result", "0"); // 로그인 실패

		String jsonObject = gson.toJson(map);

		return jsonObject;
	}

	// **********************
	// 아이디 찾기
	// **********************
	@ApiOperation(value = "아이디 찾기", notes = "아이디 찾기 입니다")
	@PostMapping("/find_id")
	public String user_find_id(UserDto udto, EmailVO evo) {
		log.info("user_find_id ()...");
		log.info("result: {}", udto);

		Map<String, String> map = new HashMap<String, String>();
		
		String findId_res = service.user_find_Id(udto,evo);
		
		map.put("result", findId_res);
		String jsonObject = gson.toJson(map);

		return jsonObject;
	}

	// **********************
	// 비밀번호 찾기
	// **********************
	// 사용자가 비밀번호를 찾으면 초기화된 비밀번호를 이메일로 전송,데이터베이스에는 초기화된 비번 저장
	@ApiOperation(value = "비밀번호 찾기", notes = "비밀번호 찾기 입니다")
	@PostMapping("/find_pw")
	public String user_find_pw(UserDto udto, EmailVO evo) {
		log.info("user_find_pw ()...");
		log.info("udto{}", udto); // 넘어오는 값 출력
		
		Map<String, String> map = new HashMap<String, String>();
		
		
		String findPw_res = service.user_find_pw(udto,evo);
		
		map.put("result", findPw_res);
		String jsonObject = gson.toJson(map);

		
		return jsonObject;
	}

}// end class
