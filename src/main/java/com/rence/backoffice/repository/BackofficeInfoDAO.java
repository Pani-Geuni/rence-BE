package com.rence.backoffice.repository;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rence.backoffice.common.OperatingTime;
import com.rence.backoffice.model.BackOfficeOperatingTimeVO;
import com.rence.backoffice.model.BackOfficeOperatingTimeEntity;
import com.rence.backoffice.model.BackOfficeVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class BackofficeInfoDAO implements BackOfficeDAO {
	
	@Autowired
	OperatingTime operatingTime;
	
	@Autowired
	BackOfficeRepository repository;
	
	@Autowired
	BackOfficeOperatingTimeRepository oper_repository;

	@Override
	public BackOfficeVO backoffice_insertOK(BackOfficeVO vo) {
		vo.setBackoffice_state("W");
		vo.setApply_date(new Date());
		
		BackOfficeVO bvo2 = new BackOfficeVO();

		int flag = repository.insert_backoffice(vo, vo.getApply_date());

		if (flag == 1) {
			bvo2 = repository.select_backoffice_no(vo.getBackoffice_email());
		}
		
		return bvo2;
	}

	@Override
	public int backoffice_operating_insertOK(BackOfficeOperatingTimeVO ovo) {
		// 운영시간
		BackOfficeOperatingTimeEntity ovo2 = new BackOfficeOperatingTimeEntity();
		ovo2 = operatingTime.operatingTime(ovo, ovo2);
		
		return oper_repository.insert_operating_time(ovo2, ovo2.getMon_stime(), ovo2.getMon_etime(), ovo2.getTue_stime(), ovo2.getTue_etime(), ovo2.getWed_stime(), ovo2.getWed_etime(), 
				ovo2.getThu_stime(), ovo2.getThu_etime(), ovo2.getFri_stime(), ovo2.getFri_etime(), ovo2.getSat_stime(), ovo2.getSat_etime(), ovo2.getSun_stime(), ovo2.getSun_etime());
	}

}
