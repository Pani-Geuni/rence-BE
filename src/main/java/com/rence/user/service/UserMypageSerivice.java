package com.rence.user.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.rence.user.model.UserDto;
import com.rence.user.model.UserMypageDto;

public interface UserMypageSerivice {
	
	//마이페이지
	UserMypageDto user_mypage_select(HttpServletRequest request, HttpServletResponse response);

	//마이페이지 - 비밀번호 수정
	Map<String, String> user_pw_updateOK(UserDto udto);
	
	//마이페이지- 현재 비밀번호 확인
	Map<String, String> check_now_pw(UserDto udto);

	//미이페이지 - 프로필 수정
	int user_img_updateOK(UserDto udto, HttpServletRequest request, MultipartHttpServletRequest mtfRequest, HttpServletResponse response, MultipartFile multipartFile_user);

	//마이페이지 - 회원탙퇴
	Map<String, String> user_secedeOK(UserDto udto, HttpSession session, HttpServletRequest request, HttpServletResponse response);

	// 마이페이지 - 예약리스트
	Map<String, Object> reserve_list_rsu(String time_point, String user_no, Integer page);

}
