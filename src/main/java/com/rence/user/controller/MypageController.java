/**
 * @author 강경석
 * 마이페이지에 관련된 전반적 기술을 처리하는 컨트롤러
 * 마이페이지의 페이징
 * 회원 탈퇴
 *
 */
package com.rence.user.controller;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rence.user.model.MyPageReserveListDto;
import com.rence.user.model.UserDto;
import com.rence.user.model.UserMypageDto;
import com.rence.user.service.UserFileuploadService;
import com.rence.user.service.UserMypageSerivice;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(tags = "마이페이지 컨트롤러")
@Slf4j
@RestController
@RequestMapping("/rence")
public class MypageController {

	@Autowired
	UserMypageSerivice service;

	@Autowired
	UserFileuploadService fileuploadService;

	@Autowired
	HttpSession session;

	// 자동 개행 및 줄 바꿈 (new Gson은 일자로 나옴)
	Gson gson = new GsonBuilder().setPrettyPrinting().create();

	// **********************
	// 마이페이지
	// **********************
	@ApiOperation(value = "마이페이지", notes = "마이페이지 입니다.")
	@GetMapping("/go_my_page")
	public String go_my_page(HttpServletRequest request, HttpServletResponse response) {
		log.info("go_my_page()...");
		
		UserMypageDto umdto = service.user_mypage_select(request, response);

		
		String jsonObject = gson.toJson(umdto);
		return jsonObject;
	}

	// **********************
	// 마이페이지 - 비밀번호 수정
	// **********************
	@ApiOperation(value = "비밀번호 수정", notes = "비밀번호 수정입니다.")
	@PostMapping("/user_pw_updateOK")
	@ResponseBody
	public String user_pw_upddateOK(UserDto udto) {

		log.info("user_pw_upddateOK()...");
		log.info("result: {}", udto);

		Map<String, String> map = service.user_pw_updateOK(udto);


		String jsonObject = gson.toJson(map);
		return jsonObject;
	}

	// **********************
	// 마이페이지 - 비밀번호 수정 - 현재 비밀번호 확인(본인인증)
	// **********************
	@ApiOperation(value = "현재 비밀번호 확인(본인인증)", notes = "현재 비밀번호 확인(본인인증) 입니다.")
	@PostMapping("/check_now_pw")
	public String check_now_pw(UserDto udto) {
		log.info("check_now_pw()...");
		log.info("request: {}", udto);

		Map<String, String> map = service.check_now_pw(udto);
		String jsonObject = gson.toJson(map);

		return jsonObject;
	}

	// **********************
	// 마이페이지 -프로필 수정 AWS적용
	// **********************
	@ApiOperation(value = "프로필 수정", notes = "프로필 수정 입니다.")
	@RequestMapping(value = "/user_img_updateOK", method = RequestMethod.POST)
	public String user_img_updateOK(UserDto udto, HttpServletRequest request, HttpServletResponse response,
			MultipartHttpServletRequest mtfRequest, @RequestParam(value = "multipartFile") MultipartFile multipartFile_user) {
		log.info("user_img_updateOK()...");
		log.info("result: {}", udto);
		
		int result = service.user_img_updateOK(udto, request, mtfRequest, response, multipartFile_user);
		
		return "redirect:/rence/go_my_page";
	}

	// **********************
	// 회원탈퇴
	// **********************
	@ApiOperation(value = "회원탈퇴", notes = "회원탈퇴 입니다.")
	@RequestMapping(value = "/secedeOK", method = RequestMethod.POST)
	@ResponseBody
	public String user_secedeOK(UserDto udto, HttpServletRequest request, HttpServletResponse response) {
		log.info("user_secedeOK()...");
		log.info("result: {}", udto);

		Map<String, String> map = service.user_secedeOK(udto, session, request, response );
		
		String jsonObject = gson.toJson(map);
		return jsonObject;
	}

	
	// **********************
	// 예약 리스트 이동
	// **********************
	@ApiOperation(value = "예약리스트", notes = "예약리스트 페이지입니다.")
	@GetMapping("/reserve_list")
	public String reserve_list_rsu(String time_point, String user_no,
			@RequestParam(value = "page", defaultValue = "1") Integer page) {

		log.info("reserve_list");
		log.info("current page: {}", page);
		
		
		Map<String, Object> map = service.reserve_list_rsu(time_point, user_no, page);
		

		

		String jsonObject = gson.toJson(map);
//		return "thymeleaf/layouts/office/layout_myPage";
		return jsonObject;
	}

//	// **********************
//	// 마일리지 리스트
//	// **********************
//	@ApiOperation(value = "마일리지 리스트", notes = "마일리지 리스트 페이지입니다.")
//	@GetMapping("/mileage")
//	public String go_mileage(UserDto udto, Model model, HttpServletRequest request,
//			@RequestParam(value = "page", defaultValue = "1") Integer page) {
//
//		Map<String, Object> map = new HashMap<String, Object>();
//
//		log.info("go_mileage()...");
//		log.info("UserDto(사용자 고유번호): {}", udto);
//
//		log.info("current page: {}", page);
//
//		// 총 마일리지 부분
//		UserMileageVO umdto = service.totalMileage_selectOne(udto);
//		log.info("umdto: {}", umdto);
//
//		// 마일리지 콤마단위로 변환
//		DecimalFormat dc = new DecimalFormat("###,###,###,###,###");
//		String mileage_total = dc.format(umdto.getMileage_total());
//		log.info("mileage_total: " + mileage_total);
//
//		// 페이징 처리 로직(마일리지 리스트 전용!!!!)
//		// 리스트 수
//		long total_rowCount_mileage_all = service.total_rowCount_mileage_all(udto);
//		total_rowCount_mileage_all -= 1; // 회원가입시 들어가는 기본값 제외
//		log.info("total_rowCount_mileage_all: {}", total_rowCount_mileage_all);
//
//		// 총 페이징되는 수
//		long totalPageCnt = (long) Math.ceil(total_rowCount_mileage_all / 8.0);
//		log.info("totalPageCnt: {}", totalPageCnt);
//
//		// 현재페이지
//		long nowPage = page;
//
//		// 5page씩 끊으면 끝 페이지 번호( ex, 총 9페이지이고, 현재페이지가 6이면 maxpage = 9)
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
//		log.info("maxPage: " + maxPage);
//
//		map.put("totalPageCnt", totalPageCnt);
//		map.put("nowPage", nowPage);
//		map.put("maxPage", maxPage);
//
//		// 페이징처리를 위한 페이지 계산 로직끝
//
//		List<UserMileageVO> vos = service.user_mileage_selectAll_paging(udto, page, total_rowCount_mileage_all);
//		log.info("vos: " + vos);
//
//		for (int i = 0; i < vos.size(); i++) {
//			vos.get(i).setMileage(dc.format(Integer.parseInt(vos.get(i).getMileage())));
//		}
//		log.info("Type change vos: {}" + vos);
//
//		map.put("list", vos);
//		map.put("page", "mileage");
//		model.addAttribute("res", map);
//		model.addAttribute("mileage_total", mileage_total);
//		model.addAttribute("searchKey", "all");
//
//		model.addAttribute("content", "thymeleaf/html/office/my_page/mileage");
//		model.addAttribute("title", "마일리지리스트");
//
//		return "thymeleaf/layouts/office/layout_myPage";
//
//	}
//
//	// **********************
//	// 마일리지 리스트 - searchKey
//	// **********************
//	@ApiOperation(value = "마일리지 조건리스트", notes = "마일리지 조건리스트 페이지입니다.")
//	@GetMapping("/mileage_search_list")
//	public String go_mileage_search_list(UserDto udto, Model model, HttpServletRequest request, String searchKey,
//			@RequestParam(value = "page", defaultValue = "1") Integer page) {
//
//		Map<String, Object> map = new HashMap<String, Object>();
//		log.info("mileage_search_list()...");
//
//		log.info("검색 키워드: " + searchKey);
//		log.info("UserDto(사용자 고유번호): {}", udto);
//
//		// 총 마일리지 부분
//		UserMileageVO umdto = service.totalMileage_selectOne(udto);
//		log.info("umdto: {}", umdto);
//
//		// 마일리지 콤마단위로 변환
//		DecimalFormat dc = new DecimalFormat("###,###,###,###,###");
//		String mileage_total = dc.format(umdto.getMileage_total());
//		log.info("mileage_total: " + mileage_total);
//
//		// 페이징 처리 로직(이거는 마일리지 전용임!!!)
//		// 리스트 수
//		long total_rowCount_mileage_search = service.total_rowCount_mileage_searchKey(udto, searchKey);
//		total_rowCount_mileage_search -= 1;// 회원가입시 들어가는 기본값 제외
//		log.info("total_rowCount_mileage_search: {}", total_rowCount_mileage_search);
//
//		// 총 페이징되는 수
//		long totalPageCnt = (long) Math.ceil(total_rowCount_mileage_search / 8.0);
//		log.info("totalPageCnt: {}", totalPageCnt);
//
//		// 현재페이지
//		long nowPage = page;
//
//		// 5page씩 끊으면 끝 페이지 번호( ex, 총 9페이지이고, 현재페이지가 6이면 maxpage = 9)
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
//		log.info("maxPage: " + maxPage);
//
//		map.put("totalPageCnt", totalPageCnt);
//		map.put("nowPage", nowPage);
//		map.put("maxPage", maxPage);
//
//		// 페이징처리를 위한 페이지 계산 로직끝
//
//		List<UserMileageVO> vos = service.user_mileage_search_list_paging(udto, searchKey, page,
//				total_rowCount_mileage_search);
//		log.info("vos: " + vos);
//
//		for (int i = 0; i < vos.size(); i++) {
//			vos.get(i).setMileage(dc.format(Integer.parseInt(vos.get(i).getMileage())));
//		}
//		log.info("Type change vos: {}" + vos);
//
//		map.put("list", vos);
//		map.put("page", "mileage");
//
//		model.addAttribute("res", map);
//		model.addAttribute("mileage_total", mileage_total);
//		model.addAttribute("searchKey", searchKey);
//
//		model.addAttribute("content", "thymeleaf/html/office/my_page/mileage");
//		model.addAttribute("title", "마일리지리스트");
//
//		return "thymeleaf/layouts/office/layout_myPage";
//	}
//
//	// **********************
//	// 후기 리스트 이동
//	// **********************
//	@ApiOperation(value = "후기 리스트", notes = "후기 리스트 입니다.")
//	@GetMapping("/review_list")
//	public String review_list(String user_no, Model model,
//			@RequestParam(value = "page", defaultValue = "1") Integer page) {
//		log.info("review_list()...");
//		log.info("user_no: " + user_no);
//
//		Map<String, Object> map = new HashMap<String, Object>();
//
//		// 페이징 처리 로직
//		// 리스트 수
//		long total_rowCount_review = service.total_rowCount_review(user_no);
//		log.info("total_rowCount_review: {}", total_rowCount_review);
//
//		// 총 페이징되는 수
//		long totalPageCnt = (long) Math.ceil(total_rowCount_review / 8.0);
//		log.info("totalPageCnt: {}", totalPageCnt);
//
//		// 현재페이지
//		long nowPage = page;
//
//		// 5page씩 끊으면 끝 페이지 번호( ex, 총 9페이지이고, 현재페이지가 6이면 maxpage = 9)
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
//		log.info("maxPage: " + maxPage);
//
//		map.put("totalPageCnt", totalPageCnt);
//		map.put("nowPage", nowPage);
//		map.put("maxPage", maxPage);
//
//		// 페이징처리를 위한 페이지 계산 로직끝
//
//		List<UserReviewVO> list = service.select_all_review_paging(user_no, page);
//
//		map.put("page", "review");
//		map.put("list", list);
//
//		model.addAttribute("res", map);
//		log.info("review_list : {}", map);
//
//		model.addAttribute("content", "thymeleaf/html/office/my_page/review_list");
//		model.addAttribute("title", "리뷰리스트");
//
//		return "thymeleaf/layouts/office/layout_myPage";
//	}
//
//	// **********************
//	// 마이페이지 - 문의 리스트
//	// **********************
//	@ApiOperation(value = "문의 리스트", notes = "문의 리스트 페이지입니다.")
//	@GetMapping("/question_list")
//	public String question_list(String user_no, Model model,
//			@RequestParam(value = "page", defaultValue = "1") Integer page) {
//		Map<String, Object> map = new HashMap<String, Object>();
//
//		// 페이징 처리 로직
//		// 리스트 수
//		long total_rowCount_question = service.total_rowCount_question(user_no);
//		log.info("total_rowCount_question: {}", total_rowCount_question);
//
//		// 총 페이징되는 수
//		long totalPageCnt = (long) Math.ceil(total_rowCount_question / 8.0);
//		log.info("totalPageCnt: {}", totalPageCnt);
//
//		// 현재페이지
//		long nowPage = page;
//
//		// 5page씩 끊으면 끝 페이지 번호( ex, 총 9페이지이고, 현재페이지가 6이면 maxpage = 9)
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
//		log.info("maxPage: " + maxPage);
//
//		map.put("totalPageCnt", totalPageCnt);
//		map.put("nowPage", nowPage);
//		map.put("maxPage", maxPage);
//
//		// 페이징처리를 위한 페이지 계산 로직끝
//
//		List<UserQuestionVO> list = service.select_all_question_paging(user_no, page);
//		if (list != null) {
//			for (UserQuestionVO vo : list) {
//				UserQuestionVO vo2 = service.select_one_answer(vo.getComment_no());
//				if (vo2 != null) {
//					vo.setAnswer_content(vo2.getComment_content());
//					vo.setAnswer_date(vo2.getComment_date());
//					vo.setState("Y");
//				} else {
//					vo.setState("N");
//				}
//			}
//		}
//
//		map.put("page", "question_list");
//		map.put("list", list);
//
//		model.addAttribute("res", map);
//
//		log.info("question_list : {}", map);
//
//		model.addAttribute("content", "thymeleaf/html/office/my_page/question_list");
//		model.addAttribute("title", "문의리스트");
//
//		return "thymeleaf/layouts/office/layout_myPage";
//	}

}// end class
