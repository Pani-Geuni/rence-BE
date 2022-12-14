
package com.rence.office.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rence.backoffice.model.BackOfficeDTO;
import com.rence.common.OptionEngToKorMap;
import com.rence.user.controller.OfficeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@Api(tags = "오피스 컨트롤러")
@RequestMapping("/office")
public class OfficeController {

	Gson gson = new GsonBuilder().setPrettyPrinting().create();

	@Autowired
	OfficeService service;

	@Autowired
	HttpSession session;

	/*
	 * 오피스(공간) 상세 페이지
	 */
	@ApiOperation(value = "공간 소개 페이지 로드 (데스크,회의실)", notes = "데스크 / 회의실 공간 소개 페이지 로드하는 컨트롤러")
	@GetMapping(value = "/space_introduce")
	public String space_intruduce(BackOfficeDTO bdto, String introduce_menu,
			@RequestParam(value = "page", defaultValue = "1") Integer page, Model model) {

		Map<String, Object> map = new HashMap<String, Object>();

		OptionEngToKorMap info_map = new OptionEngToKorMap();
		String backoffice_no = bdto.getBackoffice_no();

		// ******************
		// backoffice 기본 정보
		// ******************
//		OfficeInfoVO ovo = service.select_one_office_info(backoffice_no);
//		List<String> type_list = new ArrayList<String>();
//		List<String> tag_list = new ArrayList<String>();
//		List<String> img_list = new ArrayList<String>();
//		List<String> option_list = new ArrayList<String>();
//		List<String> around_option_list = new ArrayList<String>();
//
//		ovo.setAvg_rating(Math.round(ovo.getAvg_rating() * 10) / 10.0);
//
//		if (ovo.getBackoffice_type() != null) {
//			type_list = info_map.splitType(ovo.getBackoffice_type());
//		} else {
//			type_list.add("타입 없음");
//		}
//
//		if (ovo.getBackoffice_tag() != null) {
//			tag_list = info_map.splitTag(ovo.getBackoffice_tag());
//		} else {
//			tag_list.add("태그 없음");
//		}
//
//		img_list = info_map.splitImage(ovo.getBackoffice_image());
//
//		if (ovo.getBackoffice_option() != null) {
//			option_list = info_map.splitOption(ovo.getBackoffice_option());
//		} else {
//			option_list.add("옵션 없음");
//		}
//
//		if (ovo.getBackoffice_around() != null) {
//			around_option_list = info_map.splitAroundOption(ovo.getBackoffice_around());
//		} else {
//			around_option_list.add("주변 시설 없음");
//		}
//
//		String short_roadname_address = info_map.makeShortAddress(ovo.getRoadname_address());
//
//		// ******************
//		// backoffice 운영 시간
//		// ******************
//		OfficeOperatingTimeVO_date dotvo = service.select_one_operating_time(backoffice_no);
//
//		CustomDateFormatter df = new CustomDateFormatter();
//		OfficeOperatingTimeVO otvo = df.showStringOfficeOperating(dotvo);
//
//		// ************************
//		// backoffice 운영 공간(Room)
//		// ************************
//		List<OfficeRoomVO> rvos = service.select_all_room(backoffice_no);
//
//		for (OfficeRoomVO vo : rvos) {
//			vo.setRoom_type(info_map.changeType(vo.getRoom_type()));
//		}
//
//		// **************
//		// backoffice 문의
//		// **************
//
//		// 페이징 처리 로직
//		// 리스트 수
//		long total_rowCount_question_all = service.total_rowCount_question_all(backoffice_no);
//		log.info("total_rowCount_question_all: {}", total_rowCount_question_all);
//
//		// 총 페이징 되는 수
//		long totalPageCnt = (long) Math.ceil(total_rowCount_question_all / 4.0);
//		log.info("totalPageCnt: {}", totalPageCnt);
//
//		long nowPage = page;
//
//		long maxPage = 0;
//
//		if (nowPage % 5 != 0) {
//			if (nowPage == totalPageCnt) {
//				maxPage = nowPage;
//			} else if (((nowPage / 5) + 1) * 5 >= totalPageCnt) {
//				maxPage = totalPageCnt;
//			} else if (((nowPage / 5) + 1) * 5 < totalPageCnt) {
//				maxPage = ((nowPage / 5) + 1) * 5;
//			}
//		} else if (nowPage % 5 == 0) {
//			if (nowPage <= totalPageCnt) {
//				maxPage = nowPage;
//			}
//		}
//
//		log.info("maxPage: " + maxPage);
//
//		map.put("totalPageCnt", totalPageCnt);
//		map.put("nowPage", nowPage);
//		map.put("maxPage", maxPage);
//
//		// 페이징 처리 계산 로직 끝
//
//		List<OfficeQuestionVO> cvos = service.select_all_comment(backoffice_no, page);
//
//		String is_login = (String) session.getAttribute("user_id");
//
//		if (cvos != null) {
//			for (OfficeQuestionVO vo : cvos) {
//
//				log.info("is_login :::::::::: {}", is_login);
//				log.info("user_no :::::::::: {}", vo.getUser_id());
//
//				OfficeQuestionVO vo2 = service.select_one_answer(vo.getComment_no());
//				if (vo2 != null) {
//					if (vo.getUser_id().equals(is_login)) {
//						vo.setAnswer_content(vo2.getComment_content());
//						vo.setAnswer_date(vo2.getComment_date());
//						vo.setComment_state("Y");
//					} else {
//						if (vo.getIs_secret() == null || vo.getIs_secret().equals("F")) {
//							vo.setAnswer_content(vo2.getComment_content());
//							vo.setAnswer_date(vo2.getComment_date());
//							vo.setComment_state("Y");
//						} else {
//							vo.setAnswer_content(null);
//							vo.setAnswer_date(null);
//						}
//					}
//				} else {
//					vo.setComment_state("N");
//				}
//
//				// 이름 마스킹
//				String originName = vo.getUser_name();
//				String firstName = originName.substring(0, 1);
//				String midName = originName.substring(1, originName.length() - 1);
//
//				String maskingMidName = "";
//				for (int i = 0; i < midName.length(); i++) {
//					maskingMidName += "*";
//				}
//
//				String lastName = originName.substring(originName.length() - 1, originName.length());
//
//				String maskingName = firstName + maskingMidName + lastName;
//
//				vo.setUser_name(maskingName);
//			}
//		}
//
//		// **************
//		// backoffice 후기
//		// **************
//
//		// 페이징 처리 로직
//		// 리스트 수
//		long total_rowCount_review_all = service.total_rowCount_review_all(backoffice_no);
//
//		// 총 페이징 되는 수
//		long totalPageCnt2 = (long) Math.ceil(total_rowCount_review_all / 4.0);
//		log.info("totalPageCnt: {}", totalPageCnt2);
//
//		long nowPage2 = page;
//		log.info("nowPage2: {}", nowPage2);
//
//		long maxPage2 = 0;
//		log.info("maxPage2 : {}", maxPage2);
//
//		if (nowPage2 % 5 != 0) {
//			if (nowPage2 == totalPageCnt2) {
//				maxPage2 = nowPage2;
//			} else if (((nowPage2 / 5) + 1) * 5 >= totalPageCnt2) {
//				maxPage2 = totalPageCnt2;
//			} else if (((nowPage2 / 5) + 1) * 5 < totalPageCnt2) {
//				maxPage2 = ((nowPage2 / 5) + 1) * 5;
//			}
//		} else if (nowPage2 % 5 == 0) {
//			if (nowPage2 <= totalPageCnt2) {
//				maxPage2 = nowPage2;
//			}
//		}
//
//		log.info("maxPage2 : {}", maxPage2);
//
//		map.put("totalPageCnt2", totalPageCnt2);
//		map.put("nowPage2", nowPage2);
//		map.put("maxPage2", maxPage2);
//		map.put("page", "space_introduce_detail");
//
//		// 페이징 처리 계산 로직 끝
//
//		List<OfficeReviewVO> revos = service.select_all_review(backoffice_no, page);
//
//		for (OfficeReviewVO vo : revos) {
//
//			// 이름 마스킹
//			String originName = vo.getUser_name();
//			String firstName = originName.substring(0, 1);
//			String midName = originName.substring(1, originName.length() - 1);
//
//			String maskingMidName = "";
//			for (int i = 0; i < midName.length(); i++) {
//				maskingMidName += "*";
//			}
//
//			String lastName = originName.substring(originName.length() - 1, originName.length());
//
//			String maskingName = firstName + maskingMidName + lastName;
//
//			vo.setUser_name(maskingName);
//		}
//
//		// backoffice 기본 정보
//		model.addAttribute("res", map);
//		model.addAttribute("ovo", ovo);
//		model.addAttribute("short_roadname_address", short_roadname_address);
//		model.addAttribute("type_list", type_list);
//		model.addAttribute("tag_list", tag_list);
//		model.addAttribute("img_list", img_list);
//		model.addAttribute("option_list", option_list);
//		model.addAttribute("around_option_list", around_option_list);
//
//		model.addAttribute("introduce_menu", introduce_menu);
//
//		// backoffice 운영 시간
//		model.addAttribute("otvo", otvo);
//
//		// backoffice 운영 공간
//		model.addAttribute("rvos", rvos);
//
//		// backoffice 문의
//		model.addAttribute("is_login", is_login);
//		model.addAttribute("cvos", cvos);
//		model.addAttribute("cvos_cnt", total_rowCount_question_all);
//
//		// backoffice 후기
//		model.addAttribute("revos", revos);
//		model.addAttribute("review_cnt", total_rowCount_review_all);
//
//		model.addAttribute("page", "space_detail");
//		model.addAttribute("content", "thymeleaf/html/office/space_detail/space_detail_introduce");
//		model.addAttribute("title", "공간 상세 페이지");

		return "thymeleaf/layouts/office/layout_base";
	}

}//end class
