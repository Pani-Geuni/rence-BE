
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@Api(tags = "오피스 컨트롤러")
@RequestMapping("/office")
public class OfficeController {

	Gson gson = new GsonBuilder().setPrettyPrinting().create();

//	@Autowired
//	OfficeService service;

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



		return "thymeleaf/layouts/office/layout_base";
	}

}//end class
