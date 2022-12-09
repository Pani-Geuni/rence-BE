package com.rence.backoffice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rence.backoffice.model.BackOfficeOperatingTimeVO;
import com.rence.backoffice.model.BackOfficeVO;
import com.rence.backoffice.repository.BackOfficeDAO;

@Service
public class BackOfficeInfoService implements BackOfficeService {
	@Autowired
	BackOfficeDAO dao;

	@Override
	public BackOfficeVO insertOK(BackOfficeVO vo, BackOfficeOperatingTimeVO ovo) {
		// 백오피스 insert
		BackOfficeVO bvo2 = dao.backoffice_insertOK(vo);

		// 운영시간 insert
		ovo.setBackoffice_no(bvo2.getBackoffice_no());
		int result = dao.backoffice_operating_insertOK(ovo);

		if (result==1) {
			return bvo2;
		}else {
			return null;
		}
		
	}

}
