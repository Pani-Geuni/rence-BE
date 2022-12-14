package com.rence.office.controller;

import java.util.Map;

import com.rence.backoffice.model.BackOfficeDTO;

public interface OfficeService {

	//오피스(공간) 상세 페이지
	Map<String, Object> space_intruducePage(BackOfficeDTO bdto, String introduce_menu, Integer page);

}
