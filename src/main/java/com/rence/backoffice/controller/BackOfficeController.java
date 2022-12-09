/**
 * 
 * @author 최진실
 *
 */
package com.rence.backoffice.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rence.backoffice.model.AuthVO;
import com.rence.backoffice.model.BackOfficeOperatingTimeVO;
import com.rence.backoffice.model.BackOfficeVO;
import com.rence.backoffice.model.EmailVO;
import com.rence.backoffice.service.BackOfficeFileService;
import com.rence.backoffice.service.BackOfficeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(tags="백오피스 컨트롤러")
@RequestMapping("/backoffice")
public class BackOfficeController {

	Gson gson = new GsonBuilder().setPrettyPrinting().create();

	@Autowired
	BackOfficeService service;

	@Autowired
	BackOfficeFileService fileService;
	
	@Autowired
	HttpSession session;
	
	/**
	 * 백오피스 신청 처리
	 * 
	 * @throws ParseException
	 */
	@ApiOperation(value="호스트 신청 처리", notes="호스트 신청 처리입니다.")
	@PostMapping("/insertOK")
	public String backoffice_insertOK(BackOfficeVO vo, BackOfficeOperatingTimeVO ovo,
		MultipartHttpServletRequest mtfRequest, @RequestParam(value = "multipartFile_room") MultipartFile multipartFile_room, @RequestParam(value = "multipartFile_host") MultipartFile multipartFile_host) throws ParseException {

		Map<String , String> map = new HashMap<String,String>();
		
		vo = fileService.backoffice_image_upload(vo, mtfRequest, multipartFile_room, multipartFile_host);
		
		BackOfficeVO bvo2 = service.insertOK(vo,ovo);

		if (bvo2 != null) {
			map.put("result", "1");
		}else {
			map.put("result", "0");
		}
		
		String json = gson.toJson(map);

		return json;
	}
	
	/**
	 * 이메일 인증번호 요청
	 */
	@ApiOperation(value="이메일 인증번호 요청", notes="호스트 신청시, 이메일 인증번호 요청 페이지입니다.")
	@GetMapping("/auth")
	public String backoffice_auth(AuthVO avo, BackOfficeVO bvo, EmailVO evo) {

		 Map<String, String> map = service.backoffice_auth(avo,bvo,evo);

		String json = gson.toJson(map);

		return json;
	}
	
	
	
}
