/**
 * 
 * @author 최진실
 *
 */
package com.rence.backoffice.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rence.backoffice.model.AuthVO;
import com.rence.backoffice.model.BackOfficeOperatingTimeVO;
import com.rence.backoffice.model.BackOfficeVO;
import com.rence.backoffice.model.EmailVO;
import com.rence.backoffice.repository.BackOfficeDAO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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

	@Override
	public Map<String, String> backoffice_auth(AuthVO avo, BackOfficeVO bvo, EmailVO evo) {
		// TODO Auto-generated method stub
		return null;
	}

}
