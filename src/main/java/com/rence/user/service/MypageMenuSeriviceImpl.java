package com.rence.user.service;



import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rence.office.common.OfficeInfoMap;
import com.rence.office.model.OfficePaymentDto;
import com.rence.user.controller.UserInfoDto;
import com.rence.user.model.ReserveInfo_ViewDto;
import com.rence.user.model.ReserveMileageDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MypageMenuSeriviceImpl implements MypageMenuSerivice {
	
	
	@Autowired
	HttpSession session;
	
	@Autowired
	MypageMenuDAO dao;

	//상세 예약 페이지 이동 - 현재
	@Override
	public Map<String, Object> reserve_info_rsu(String reserve_no, HttpServletRequest request) {
		log.info("reserve_info_rsu()...");		
		
		OfficeInfoMap info_map = new OfficeInfoMap();
		Map<String, Object> map = new HashMap<String, Object>();
		String user_no = null;
		String is_login = (String) session.getAttribute("user_id");
		Cookie[] cookies = request.getCookies();

		if (is_login != null && cookies != null) {
			for (Cookie c : cookies) {
				if (c.getName().equals("user_no")) {
					user_no = c.getValue();
				}
			}

			

			ReserveInfo_ViewDto dto = dao.select_one_reserve_info(reserve_no);

			List<String> splitImage = info_map.splitImage(dto.getBackoffice_image());
			String room_first_image = splitImage.get(0);
			dto.setBackoffice_image(room_first_image);

			dto.setRoom_type(info_map.changeType(dto.getRoom_type()));

			map.put("reserve_no", reserve_no);
			map.put("info_obj", dto);

			if (dto != null) {
				DecimalFormat dc = new DecimalFormat("###,###,###,###");

				String ch1 = dc.format(Integer.parseInt(dto.getActual_payment()));
				dto.setActual_payment(ch1);

				String ch2 = dc.format(Integer.parseInt(dto.getMileage_change()));
				dto.setMileage_change(ch2);

				String ch3 = dc.format(Integer.parseInt(dto.getPayment_total()));
				dto.setPayment_total(ch3);

				String ch4 = dc.format(Integer.parseInt(dto.getRoom_price()));
				dto.setRoom_price(ch4);
			}

			ReserveMileageDto mdto = dao.select_one_reserve_mileage(reserve_no);

			if (mdto != null) {
				DecimalFormat dc = new DecimalFormat("###,###,###,###");

				if (mdto.getMileage_state().equals("F")) {
					mdto.setMileage_change(
							Integer.toString((int) Math.round(Integer.parseInt(mdto.getActual_payment()) * 0.05)));
				} 
				
				String ch1 = dc.format(Integer.parseInt(mdto.getActual_payment()));
				mdto.setActual_payment(ch1);

				String ch2 = dc.format(Integer.parseInt(mdto.getPayment_total()));
				mdto.setPayment_total(ch2);

				

				String ch3 = dc.format(Integer.parseInt(mdto.getMileage_change()));
				mdto.setMileage_change(ch3);

				String ch4 = dc.format(Integer.parseInt(mdto.getUse_mileage()));
				mdto.setUse_mileage(ch4);
			}

			map.put("mdto", mdto);

			// 버튼 section
			OfficePaymentDto pdto = dao.select_one_cancel_payment(reserve_no);
			map.put("pdto", pdto);

			UserInfoDto dto2 = dao.select_one_user_info(user_no);
			map.put("user_obj", dto2);
		}
		return map;
	}
	
	// 상세 예약 페이지 - 예약 취소
	@Override
	public Map<String, Object> reserve_cancel_rsu(String reserve_no, String user_no) {
		log.info("reserve_cancel_rsu()...");
		
		Map<String, String> map = new HashMap<String, String>();

		int result = dao.update_reserve_cancel(reserve_no, user_no);
		int mileage_result = 0;
		

//		OfficePaymentVO pvo = officeService.select_one_cancel_payment(reserve_no);
//
//		List<OfficeMileageVO> mvo = officeService.select_all_mileage_cancel(pvo.getPayment_no());
//
//		log.info("menu mvo :: {}", mvo);
//
//		for (OfficeMileageVO vo : mvo) {
//			String mileage_no = vo.getMileage_no();
//
//			log.info("menu vo :: {}", vo);
//
//			// 마일리지 사용 취소
//			if (vo.getMileage_state().equals("F")) {
//				OfficeMileageVO temp_vo = officeService.select_one_mileage_cancel(pvo.getPayment_no(), "F");
//
//				temp_vo.setMileage_no(null);
//				temp_vo.setMileage_state("T");
//				temp_vo.setMileage_total(temp_vo.getMileage_total() + temp_vo.getMileage_change());
//
//				log.info("temp_Vo :: {}", temp_vo);
//
//				mileage_result = officeService.insert_mileage_changed(temp_vo);
//			}
//
//			// 예약 취소 시, 마일리지 상태를 적립 예정(W)에서 적립 예정 취소(C)로 변경
//			if (vo.getMileage_state().equals("W")) {
//				mileage_result = officeService.update_mileage_state(mileage_no);
//			}
//		}
//
//		if (result == 1 && mileage_result == 1) {
//			map.put("result", "1");
//		} else {
//			map.put("result", "0");
//		}
		
		return null;

	}

}//end class
