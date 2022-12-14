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
import com.rence.office.model.OfficeMileageDto;
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
	
//	@Autowired
//	PaymentCancelDAO paymentService;

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
		log.info("map_res: {}", map);	
		return map;
	}
	
	//상세 예약 페이지 - 예약 취소
	@Override
	public Map<String, String> reserve_cancel_rsu(String reserve_no, String user_no) {
		log.info("reserve_cancel_rsu()...");
		
		Map<String, String> map = new HashMap<String, String>();

		int result = dao.update_reserve_cancel(reserve_no, user_no);
		int mileage_result = 0;
		

		OfficePaymentDto pdto = dao.select_one_cancel_payment(reserve_no);
		
		
		
		List<OfficeMileageDto> mdto = dao.select_all_mileage_cancel(pdto.getPayment_no());

		log.info("menu mdto :: {}", mdto);

		for (OfficeMileageDto dto : mdto) {
			String mileage_no = dto.getMileage_no();

			log.info("menu dto :: {}", dto);

			// 마일리지 사용 취소
			if (dto.getMileage_state().equals("F")) {
				OfficeMileageDto temp_vo = dao.select_one_mileage_cancel(pdto.getPayment_no(), "F");

				temp_vo.setMileage_no(null);
				temp_vo.setMileage_state("T");
				temp_vo.setMileage_total(temp_vo.getMileage_total() + temp_vo.getMileage_change());

				log.info("temp_Vo :: {}", temp_vo);

				mileage_result = dao.insert_mileage_changed(temp_vo);
			}

			// 예약 취소 시, 마일리지 상태를 적립 예정(W)에서 적립 예정 취소(C)로 변경
			if (dto.getMileage_state().equals("W")) {
				mileage_result = dao.update_mileage_state(mileage_no);
			}
		}

		if (result == 1 && mileage_result == 1) {
			map.put("result", "1");
		} else {
			map.put("result", "0");
		}
		log.info("map_res: {}", map);	
		return map;
	}
	
	// 예약 취소 후 결제 취소
	@Override
	public Map<String, String> payment_cancel_rsu(String reserve_no, Integer cancel_amount, String reason) {
		
		log.info("payment_cancel_rsu().... ");	
		
		Map<String, String> map = new HashMap<>();

		OfficePaymentDto pdto = dao.select_one_cancel_payment(reserve_no);
		

//		String token = paymentService.getToken();
//		int amount = pvo.getActual_payment();
//		paymentService.payMentCancel(token, pvo.getImp_uid(), amount, "사용자 예약 취소");
//
//		map.put("reserve_no", reserve_no);
//
//		Integer update_cancel_amount = pvo.getActual_payment();
//
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//		Date payment_date = pvo.getPayment_date();
//		Date current_date = new Date();
//
//		@SuppressWarnings("deprecation")
//		long diffHour = (current_date.getTime() - payment_date.getTime()) / 3600000;
//
//		if (diffHour <= 1) {
//			update_cancel_amount = pvo.getActual_payment();
//		} else {
//			Integer deposit = (int) (pvo.getPayment_total() * 0.2);
//
//			update_cancel_amount = pvo.getActual_payment() - deposit;
//		}
//
//		int result = officeService.update_payment_cancel(reserve_no, update_cancel_amount);
//
//		if (result == 1) {
//			map.put("result", "1");
//		} else {
//			map.put("result", "0");
//		}

		return map;
	}

}//end class
