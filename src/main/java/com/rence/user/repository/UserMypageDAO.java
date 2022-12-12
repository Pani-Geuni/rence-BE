package com.rence.user.repository;

import com.rence.user.model.UserDto;
import com.rence.user.model.UserMypageDto;

public interface UserMypageDAO {

	//마이페이지 정보 불러오기
	UserMypageDto user_mypage_select(String user_no);

	//마이페이지 - 비밀번호 수정
	int user_pw_updateOK(UserDto udto);
	
	//마이페이지 - 비밀번호 수정 - 현재 비밀번호 확인 
	int check_now_pw(UserDto udto);

	//마이페이지 - 프로필 변경내용 저장
	int user_img_updateOK(UserDto udto);

	//마이페이지 - 회원탈퇴
	int user_secedeOK(UserDto udto);
 
	//마이페이지 - 예약리스트 - 총 예약리스트 수
	long total_rowCount_reserve(String user_no, String time_point);

}//end class
