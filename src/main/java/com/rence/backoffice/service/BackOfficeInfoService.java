/**
 * 
 * @author 최진실
 *
 */
package com.rence.backoffice.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.rence.backoffice.common.BackOfficeSendEmail;
import com.rence.backoffice.model.AuthVO;
import com.rence.backoffice.model.BackOfficeOperatingTimeVO;
import com.rence.backoffice.model.BackOfficeVO;
import com.rence.backoffice.model.EmailVO;
import com.rence.backoffice.repository.BackOfficeDAO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BackOfficeInfoService implements BackOfficeService {
	@Autowired
	BackOfficeDAO dao;

	@Autowired
	BackOfficeSendEmail authSendEmail;
	
	@Autowired
	HttpSession session;

	/**
	 * 백오피스 신청 처리
	 */
	@Override
	public Map<String, String> insertOK(BackOfficeVO vo, BackOfficeOperatingTimeVO ovo) {

		Map<String, String> map = new HashMap<String, String>();

		// 백오피스 insert
		BackOfficeVO bvo2 = dao.backoffice_insertOK(vo);

		// 운영시간 insert
		ovo.setBackoffice_no(bvo2.getBackoffice_no());
		int result = dao.backoffice_operating_insertOK(ovo);

		if (result == 1) {
			map.put("result", "1");
		} else {
			map.put("result", "0");
		}

		return map;

	}

	/**
	 * 이메일 인증번호 요청
	 */
	@Override
	public Map<String, String> backoffice_auth(AuthVO avo, BackOfficeVO bvo, EmailVO evo) {
		Map<String, String> map = new HashMap<String, String>();

		// 이메일 중복 체크
		BackOfficeVO emailCheck = dao.backoffice_email_check(bvo);
		AuthVO avo_check = dao.backoffice_auth_overlap(bvo);

		if (emailCheck == null || emailCheck.getBackoffice_state().equals("X")
				|| emailCheck.getBackoffice_state().equals("N")) {
			if (avo_check == null) {

				avo.setUser_email(bvo.getBackoffice_email());

				// 이메일 전송
				avo = authSendEmail.sendEmail(avo, evo);
				if (avo != null) {

					// avo2 = auth 테이블에 정보 저장 후, select 해온 결과값
					AuthVO avo2 = dao.backoffice_auth_insert(avo);
					log.info("successed...");

					map.put("result", "1");
					map.put("auth_code", avo2.getAuth_code());
					map.put("backoffice_email", avo2.getUser_email());
					map.put("auth_no", avo2.getAuth_no());

				} else { // 전송 실패
					log.info("failed...");
					map.put("result", "0");
				}
			} else { // 재전송 실패
				log.info("failed...3");
				map.put("result", "3");
			}
		} else { // 중복체크 실패
			log.info("failed...");
			map.put("result", "0");
		}

		return map;
	}

	/**
	 * 이메일 인증번호 확인
	 */
	@Override
	public Map<String, String> backoffice_authOK(AuthVO avo, String backoffice_email, String auth_code) {

		AuthVO avo2 = dao.backoffice_authOK_select(backoffice_email, auth_code);

		Map<String, String> map = new HashMap<String, String>();

		if (avo2 != null) {
			log.info("successed...");
			map.put("result", "1");
			dao.backoffice_auth_delete(avo2);

		} else {
			log.info("failed...");
			map.put("result", "0");
		}

		return map;
	}

	/**
	 * 로그인 성공 처리
	 */
	@Override
	public Map<String, String> backoffice_loginOK(String username, HttpServletResponse response, HttpSession session) {

		BackOfficeVO bvo = dao.backoffice_login_info(username);

		Map<String, String> map = new HashMap<String, String>();

		session.setAttribute("backoffice_id", bvo.getBackoffice_id());
		Cookie cookie_no = new Cookie("backoffice_no", bvo.getBackoffice_no());
		Cookie cookie_profile = new Cookie("host_image", bvo.getHost_image());
		map.put("result", "1");
		response.addCookie(cookie_no);
		response.addCookie(cookie_profile);

		return map;
	}

	/**
	 * 비밀번호 초기화(찾기), 이메일로 임시 비밀번호 전송
	 */
	@Override
	public Map<String, String> backoffice_reset_pw(BackOfficeVO bvo, EmailVO evo) {
		Map<String, String> map = new HashMap<String, String>();

		BackOfficeVO bvo2 = dao.backoffice_id_email_select(bvo);

		if (bvo2 != null) {
			bvo2 = authSendEmail.findPw(bvo2, evo);

			if (bvo2 != null) {
				int flag = dao.backoffice_resetOK_pw(bvo2);
				if (flag==1) {
					map.put("result", "1");
				}
				log.info("save failed...");
				map.put("result", "0");
			} else {
				log.info("update failed...");
				map.put("result", "0");
			}
		} else {
			log.info("send failed...");
			map.put("result", "0");
		}

		return map;
	}

	/**
	 * 비밀번호 초기화 완료
	 */
	@Override
	public Map<String, String> backoffice_settingOK_pw(BackOfficeVO bvo, HttpServletRequest request,
			HttpServletResponse response) {

		Cookie[] cookies = request.getCookies();
		String backoffice_no = "";
		if (cookies!=null) {
			for(Cookie cookie:cookies){
				if (cookie.getName().equals("backoffice_no")) {
					backoffice_no = cookie.getValue();
				}
			}		
		}

		bvo.setBackoffice_pw(new BCryptPasswordEncoder().encode(bvo.getBackoffice_pw()));
		int result = dao.backoffice_settingOK_pw(bvo);
		
		Map<String, String> map = new HashMap<String,String>();
		
		
		if (result == 1) {
			if (session.getAttribute("backoffice_id") != null) {
				if(backoffice_no.equals(bvo.getBackoffice_no())) {
					// HOST 로그인 session이 존재할 때
					// Host 환경설정 > 비밀번호 수정
					log.info("succeed...setting");
					map.put("result", "1");
				}
			} else {
				// 가입 신청이 완료되어
				// 신청자의 메일에서 링크 페이지를 열고 수정 했을 때
				log.info("succeed...landing");
				map.put("result", "2");
			}
		} else if (result == 0) {
			log.info("fail...");
			map.put("result", "0");
		}
		
		return map;
	}

}
