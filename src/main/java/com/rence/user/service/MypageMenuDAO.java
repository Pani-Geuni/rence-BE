package com.rence.user.service;

import java.util.List;

import com.rence.office.model.OfficeMileageDto;
import com.rence.office.model.OfficePaymentDto;
import com.rence.user.controller.UserInfoDto;
import com.rence.user.model.ReserveInfo_ViewDto;
import com.rence.user.model.ReserveMileageDto;
import com.rence.user.model.UserDto;

public interface MypageMenuDAO {

	
	ReserveInfo_ViewDto select_one_reserve_info(String reserve_no);

	ReserveMileageDto select_one_reserve_mileage(String reserve_no);

	OfficePaymentDto select_one_cancel_payment(String reserve_no);

	UserInfoDto select_one_user_info(String user_no);

	int update_reserve_cancel(String reserve_no, String user_no);

	List<OfficeMileageDto> select_all_mileage_cancel(String payment_no);

	OfficeMileageDto select_one_mileage_cancel(String payment_no, String string);

	int insert_mileage_changed(OfficeMileageDto temp_vo);

	int update_mileage_state(String mileage_no);

	
}//end class
