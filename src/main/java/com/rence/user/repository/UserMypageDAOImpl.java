package com.rence.user.repository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.rence.user.model.UserDto;
import com.rence.user.model.UserEntity;
import com.rence.user.model.UserMypageDto;
import com.rence.user.model.UserMypageEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class UserMypageDAOImpl implements UserMypageDAO {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	MypageRepository repository;
	
	@Autowired
	ModelMapper modelmapper;
	
	@Autowired
	MyReserveRepository myReserveRepository;

	//마이페이지 정보 불러오기
	@Override
	public UserMypageDto user_mypage_select(String user_no) {
		log.info("user_mypage_select()....");
		log.info("user_no: {}", user_no);
		
		UserMypageEntity entity = repository.user_mypage_select(user_no);
		
		UserMypageDto mypagedto = modelmapper.map(entity, UserMypageDto.class);

		return mypagedto;
	}
	
	//마이페이지 - 비밀번호 수정
	@Override
	public int user_pw_updateOK(UserDto udto) {
		log.info("user_pw_updateOK()....");
		log.info("udto: {}", udto);

		// 비밀번호 암호화 암호화
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		udto.setUser_pw(encoder.encode(udto.getUser_pw()));

		log.info("(비밀번호 변경부분)암호화된 PW: {}", udto.getUser_pw());

		return userRepository.user_pw_updateOK(udto.getUser_pw(), udto.getUser_no());
	}
	
	//마이페이지 - 비밀번호 수정 - 현재 비밀번호 확인 
	@Override
	public int check_now_pw(UserDto udto) {
		log.info("check_now_pw()....");
		log.info("(비밀번호 확인부분)uvo: {}", udto);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		UserEntity entity = userRepository.check_now_pw_selectOne(udto.getUser_no());
		log.info("(비밀번호 확인부분)entity: {}", entity);

		boolean match_res = encoder.matches(udto.getPassword(), entity.getUser_pw());
		log.info("(비밀번호 확인부분)res: {}", match_res);
		int result;
		if (match_res == true) {
			result = 1;
		} else {
			result = 0;
		}
		//udto.setUser_pw("$2a$12$SkMR3vslquCDjRVoFWGtI.XoN8Bs8DsWfrYqHb.jSt6IL3EgXLVeC"); //test1234!	
		return result;
	}
	
	//마이페이지 - 프로필 변경내용 저장
	@Override
	public int user_img_updateOK(UserDto udto) {
		log.info("user_img_updateOK()....");
		log.info("udto", udto);

		return repository.user_img_updateOK(udto.getUser_image(), udto.getUser_no());
	}
	
	// 회원탈퇴에 따른 회원상태 수정
	@Override
	public int user_secedeOK(UserDto udto) {
		log.info("user_secedeOK()....");
		log.info("udto: {}", udto);

		return repository.user_secedeOK(udto.getUser_no());
	}
	
	//마이페이지 - 예약리스트 - 총 예약리스트 수
	@Override
	public long total_rowCount_reserve(String user_no, String time_point) {
		log.info("total_rowCount_reserve_now()....");
		log.info("user_no: {}", user_no);
		log.info("time_point: {}", time_point);
		long total_rowCount_reserve = 0;
		if (time_point.equals("now")) {
			total_rowCount_reserve = myReserveRepository.count_Reserve_now(user_no);
		} else if (time_point.equals("before")) {
			total_rowCount_reserve = myReserveRepository.count_Reserve_before(user_no);
		}

		return total_rowCount_reserve;
	}
	
	
}//end class
