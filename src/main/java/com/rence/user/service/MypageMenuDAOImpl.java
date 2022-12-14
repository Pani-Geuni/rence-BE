package com.rence.user.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rence.office.model.OfficePaymentDto;
import com.rence.office.model.OfficePaymentEntity;
import com.rence.office.repo.OfficeReserveRepository;
import com.rence.user.controller.UserInfoDto;
import com.rence.user.model.ReserveInfo_ViewDto;
import com.rence.user.model.ReserveInfo_ViewEntity;
import com.rence.user.model.ReserveMileageDto;
import com.rence.user.model.ReserveMileageEntity;
import com.rence.user.model.UserDto;
import com.rence.user.model.UserEntity;
import com.rence.user.repository.MypageMenuRepository;
import com.rence.user.repository.OfficePaymentRepository;
import com.rence.user.repository.ReserveMileageRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class MypageMenuDAOImpl implements MypageMenuDAO {

	@Autowired
	ModelMapper modelmapper;

	@Autowired
	MypageMenuRepository menuRepository;

	@Autowired
	ReserveMileageRepository reserve_mileage_repository;

	@Autowired
	OfficePaymentRepository payment_repository;
	
	@Autowired
	OfficeReserveRepository reserve_repository;
	
	

	// 예약 상세정보
	@Override
	public ReserveInfo_ViewDto select_one_reserve_info(String reserve_no) {
		log.info("select_one_reserve_info()...");
		log.info("reserve_no: {}", reserve_no);

		ReserveInfo_ViewEntity entity = menuRepository.select_one_reserve_info(reserve_no);

		ReserveInfo_ViewDto dto = modelmapper.map(entity, ReserveInfo_ViewDto.class);

		return dto;
	}

	// 예약 마일리지 정보
	@Override
	public ReserveMileageDto select_one_reserve_mileage(String reserve_no) {
		log.info("select_one_reserve_mileage()...");
		log.info("reserve_no: {}", reserve_no);

		ReserveMileageEntity entity = reserve_mileage_repository.select_one_reserve_mileage(reserve_no);
		ReserveMileageDto dto = modelmapper.map(entity, ReserveMileageDto.class);

		return dto;
	}

	@Override
	public OfficePaymentDto select_one_cancel_payment(String reserve_no) {
		log.info("select_one_cancel_payment()...");
		log.info("reserve_no: {}", reserve_no);

		OfficePaymentEntity entity = payment_repository.select_one_cancel_payment(reserve_no);
		OfficePaymentDto dto = modelmapper.map(entity, OfficePaymentDto.class);

		return dto;
	}

	@Override
	public UserInfoDto select_one_user_info(String user_no) {
		log.info("select_one_cancel_payment()...");
		log.info("reserve_no: {}", user_no);

		UserEntity entity = menuRepository.select_one_user_info(user_no);
		UserInfoDto dto = modelmapper.map(entity, UserInfoDto.class);

		return dto;
	}

	@Override
	public int update_reserve_cancel(String reserve_no, String user_no) {
		log.info("update_reserve_cancel()...");
		log.info("reserve_no: {}", reserve_no);
		log.info("reserve_no: {}", user_no);

		int result = reserve_repository.update_reserve_cancel(reserve_no, user_no);

		return result;
	}

}// end class
