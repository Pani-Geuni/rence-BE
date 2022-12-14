package com.rence.user.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface MypageMenuSerivice {

	// 상세 예약 페이지 이동 - 현재
	Map<String, Object> reserve_info_rsu(String reserve_no, HttpServletRequest request);

	// 상세 예약 페이지 - 예약 취소
	Map<String, Object> reserve_cancel_rsu(String reserve_no, String user_no);

}//end class
