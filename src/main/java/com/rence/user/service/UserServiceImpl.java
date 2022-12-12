package com.rence.user.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rence.backoffice.model.AuthVO;
import com.rence.backoffice.repository.BackOfficeDAO;
import com.rence.backoffice.service.BackOfficeInfoService;
import com.rence.user.model.EmailVO;
import com.rence.user.model.UserDto;
import com.rence.user.repository.UserDAO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	UserDAO dao;

	@Autowired
	UserSendEmail authSendEmail;

	@Override
	// 로그인 성공로직
	public UserDto user_login_info(String username) {
		log.info("user_login_info()...");
		log.info("username: {}", username);

		return dao.user_login_info(username);
	}

	@Override
	// 이메일 체크
	public String user_EmailCheckOK(UserDto udto, AuthVO avo, EmailVO evo) {
		log.info("user_EmailCheckOK()....");
		log.info("udto: {}", udto);
		log.info("avo: {}", avo);
		log.info("evo: {}", evo);

		String emailCheck_result = null;

		// 이메일 중복 체크
		UserDto emailCheck = dao.emailCheckOK(udto);
		log.info("emailCheck: {}", emailCheck);

		// 탈퇴한 회원의 이메일로 재가입 가능
		if (emailCheck == null || emailCheck.getUser_state().equalsIgnoreCase("N   ")) {
			avo.setUser_email(udto.getUser_email());
			log.info("avo : {}", avo);
			// 인증 테이블에 인증한 기록이 있는지 확인(카운트) 1이상이면 인증을 시도를 한 상태
			int auth_selectCnt = dao.user_auth_selectCnt(avo);
			// 인증테이블에 데이터가 없을때(첫 시도 or 2분경과로 자동삭제가 된 상태)
			if (auth_selectCnt == 0) {
				// 이메일 전송
				avo = authSendEmail.sendEmail(avo, evo);
				log.info("메일이 전송되었습니다.C_avo: {}", avo);
			}
			if (avo != null) {
				// 인증테이블에 데이터가 있을때(재시도, 2분경과가 되지 않은 상태)
				if (auth_selectCnt > 0) {
					// 인증번호 재전송 시간전에 재요청시
					log.info("user_auth Re-try authentication");

					emailCheck_result = "3";
				} else {
					log.info("auth_selectCnt:{}", auth_selectCnt);
					AuthVO avo2 = dao.user_auth_insert(avo);
					log.info("user_auth successed...");
					log.info("avo2:{}", avo2);
					emailCheck_result = "1";
				}
			} else {
				log.info("user_auth failed...");
				emailCheck_result = "0";
			}
		}
		// 이메일 중복체크시 이메일이 있을때(회원이 가입이 되어 있는상태) 2반환
		else {
			log.info("user_auth failed...(email check NOT OK)");
			emailCheck_result = "2";
		}

		return emailCheck_result;
	}

	@Override
	public String user_AuthOK(String user_email, String email_code) {
		log.info("user_AuthOK...");
		log.info("user_email: {}", user_email);
		log.info("email_code: {}", email_code);

		String user_auth_result = null;

		AuthVO avo = dao.user_authOK_select(user_email, email_code);
		log.info("avo: {}", avo);

		if (avo != null) {
			log.info("successed...");
			int del_result = dao.user_auth_delete(user_email, email_code);
			log.info("del_result: ", del_result);

			user_auth_result = "1";
		} else {
			log.info("failed...");
			user_auth_result = "0";
		}

		return user_auth_result;
	}

	@Override
	public String idCheckOK(UserDto udto) {
		log.info("user_idCheckOK()...");
		log.info("result: {}", udto);

		String idCheck_result = null;

		log.info("idCheck_result: {}", idCheck_result);
		int idCheck = dao.idCheckOK(udto.getUser_id());

		if (idCheck == 0) {
			idCheck_result = "1"; // 아이디 사용가능("OK")
		} else {
			// udto가 null이 아니면 아이디 존재
			idCheck_result = "0"; // 아이디 존재("NOT OK")
		}

		return idCheck_result;
	}

	@Override
	// 회원가입
	public int user_insertOK(UserDto udto) {
		log.info("user_insertOK()....");
		log.info("udto: {}", udto);

		int join_result = 0;
		int insert_result = dao.user_insertOK(udto);

		// 회원정보 입력 성공시
		if (insert_result == 1) {
			String user_no = dao.user_select_userno();
			log.info("user_no: {}", user_no);
			int mileage_insert_result = dao.user_mileage_zero_insert(user_no);
			if (mileage_insert_result == 1) {
				join_result = 1;
			}
		} else {
			// 회원가입은 했지만 마일리지 데이터가 안들어갔으므로 실패
			join_result = 0;
		}

		return join_result;
	}

	// 아이디 찾기
	@Override
	public String user_find_Id(UserDto udto, EmailVO evo) {
		log.info("user_find_Id()...");
		log.info("udto: {}", udto);

		String findId_res = null;

		UserDto udto2 = dao.user_email_select(udto);
		log.info("udto2: {}", udto2);
		if (udto2 != null) {
			udto2 = authSendEmail.findId(udto2, evo); // 유저의 메일로 아이디 전송

			if (udto2 != null) {
				log.info("user_fine_id successed...");
				findId_res = "1";

			} else {
				log.info("user_fine_id failed...");
				findId_res = "0";
			}
		}
		return findId_res;
	}

	//비밀번호 찾기
	@Override
	public String user_find_pw(UserDto udto, EmailVO evo) {
		log.info("user_find_pw()...");
		log.info("udto: {}", udto);
		
		String findPw_res = null;
		
		UserDto udto2 = dao.user_id_email_select(udto); // 아이디 이메일 체크
		
		if (udto2 != null) {
			// udto2가 null이 아니면(테이블에 데이터가 존재하면) 메일을 통해 수정링크 제공
			udto2 = authSendEmail.findPw(udto2, evo);
			log.info("비밀번호 찾기 메일 전송완료");
			int result = dao.user_pw_init(udto2);
			log.info("비밀번호 초기화 업데이트 완료");

			if (result != 0) {
				log.info("user_fine_pw successed...");
				findPw_res = "1";
			} else {
				log.info("user_fine_pw failed...");
				findPw_res = "0";
			}

		}
		return findPw_res;
	}

}// end class