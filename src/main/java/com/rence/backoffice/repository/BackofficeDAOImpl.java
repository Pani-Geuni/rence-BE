/**
 * 
 * @author 최진실
 *
 */
package com.rence.backoffice.repository;

import java.util.Date;
import java.util.Base64;
import java.util.Base64.Decoder;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import com.rence.backoffice.common.OperatingTime;
import com.rence.backoffice.model.AuthVO;
import com.rence.backoffice.model.BackOfficeVO;
import com.rence.backoffice.model.BackOfficeOperatingTimeEntity;
import com.rence.backoffice.model.BackOfficeOperatingTimeVO;
import com.rence.backoffice.model.BackOfficeVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class BackofficeDAOImpl implements BackOfficeDAO {

	@Autowired
	OperatingTime operatingTime;

	@Autowired
	BackOfficeRepository repository;

	@Autowired
	BackOfficeOperatingTimeRepository oper_repository;

	@Autowired
	AuthRepository auth_repository;
	
//	@Bean
//	public ModelMapper modelMapper() {
//		return new ModelMapper();
//	}
//
//	@Autowired
//	ModelMapper modelMapper;
	/**
	 * 백오피스 신청 - 업체 정보
	 */
	@Override
	public BackOfficeVO backoffice_insertOK(BackOfficeVO vo) {
		vo.setBackoffice_state("W");
		vo.setApply_date(new Date());

		BackOfficeVO bvo2 = new BackOfficeVO();

		int flag = repository.insert_backoffice(vo, vo.getApply_date());

		if (flag == 1) {
			bvo2 = repository.select_backoffice_no(vo.getBackoffice_email());
//			BackOfficeEntity bvo = repository.select_backoffice_no(vo.getBackoffice_email());
//			ModelMapper modelMapper = new ModelMapper();
//			bvo2 = modelMapper.map(bvo, BackOfficeVO.class);
		}
		return bvo2;
	}

	/**
	 * 백오피스 신청 - 운영 시간
	 */
	@Override
	public int backoffice_operating_insertOK(BackOfficeOperatingTimeVO ovo) {
		BackOfficeOperatingTimeEntity ovo2 = operatingTime.operatingTime(ovo);
		return oper_repository.insert_operating_time(ovo2);
	}

	/**
	 * 이메일 인증번호 요청 - 이메일 중복 체크
	 */
	@Override
	public BackOfficeVO backoffice_email_check(BackOfficeVO bvo) {
		return repository.backoffice_email_check(bvo.getBackoffice_email());
	}

	/**
	 * 이메일 인증번호 요청 - 인증 번호 재전송 가능 여부
	 */
	@Override
	public AuthVO backoffice_auth_overlap(BackOfficeVO bvo) {
		return auth_repository.backoffice_auth_overlap(bvo.getBackoffice_email());
	}

	/**
	 * 이메일 인증번호 요청
	 */
	@Override
	public AuthVO backoffice_auth_insert(AuthVO avo) {
		avo.setAuth_stime(new Date());
		int result = auth_repository.insert_auth_info(avo, avo.getAuth_stime());

		AuthVO avo2 = new AuthVO();
		if (result != 0) {
			avo2 = auth_repository.findbyAuth(avo.getUser_email());
		}

		return avo2;
	}

	/**
	 * 이메일 인증번호 요청 - 인증 번호 유효 시간 초과 시, 삭제
	 */
	public void auth_auto_delete(String user_email) {
		log.info("auth_auto_delete()....");
		auth_repository.auth_auto_delete(user_email);
	}

	/**
	 * 이메일 인증 확인
	 * 
	 */
	@Override
	public AuthVO backoffice_authOK_select(String backoffice_email, String auth_code) {
		return auth_repository.findbyAuthOK(backoffice_email, auth_code);
	}

	/**
	 * 이메일 인증 확인 - 인증된 번호 삭제
	 * 
	 */
	@Override
	public void backoffice_auth_delete(AuthVO avo2) {
		auth_repository.deleteByAuth_no(avo2.getAuth_no());
	}

	/**
	 * 로그인 성공 후 정보 가져오기
	 * 
	 */
	@Override
	public BackOfficeVO backoffice_login_info(String username) {
		return repository.backoffice_login_info(username);
	}

	/**
	 * 비밀번호 초기화(찾기) - 기존 정보 유무
	 */
	@Override
	public BackOfficeVO backoffice_id_email_select(BackOfficeVO bvo) {
		return repository.select_backoffice_by_id_email(bvo.getBackoffice_id(), bvo.getBackoffice_email());
	}

	/**
	 * 비밀번호 초기화(찾기) - 임시 비밀번호 저장
	 */
	@Override
	public int backoffice_resetOK_pw(BackOfficeVO bvo2) {
		return repository.update_backoffice_temp_pw(bvo2.getBackoffice_pw(), bvo2.getBackoffice_no());
		
	}

	/**
	 * 비밀번호 초기화 완료
	 */
	@Override
	public int backoffice_settingOK_pw(BackOfficeVO bvo) {
		Decoder decoder = Base64.getDecoder();
		byte[] decodedBytes2 = decoder.decode(bvo.getBackoffice_no());
		bvo.setBackoffice_no(new String(decodedBytes2));
		return repository.update_backoffice_temp_pw(bvo.getBackoffice_pw(), bvo.getBackoffice_no());
	}

}
