/**
 * @author 최진실
 */
package com.rence.master.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rence.backoffice.model.BackOfficeListVO;
import com.rence.backoffice.model.BackOfficeVO;
import com.rence.backoffice.repository.BackOfficeListRepository;
import com.rence.backoffice.repository.BackOfficeRepository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class MasterInfoDAO implements MasterDAO{
	
	@Autowired
	MasterRepository m_repository;

	@Autowired
	BackOfficeRepository b_repository;
	
	@Autowired
	BackOfficeListRepository b_list_repository;

	/**
	 * 마스터 메인 페이지 (호스트 가입 신청 리스트)
	 */
	@Override
	public List<BackOfficeListVO> backoffice_applyList_selectAll(Integer page) { // 커버드 인덱스
		log.info("backoffice_applyList_selectAll().....");
		log.info("currentpage:{}", page);

		Integer row_count = 15;
		Integer start_row = (page - 1) * row_count + 1;
		Integer end_row = page * row_count;

		return b_list_repository.selectAll_backoffice_apply(start_row, end_row);
	}

	/**
	 * 마스터 메인 페이지 (호스트 가입 신청 리스트 - 승인)
	 * 
	 */
	@Override
	public int backoffice_grant(BackOfficeVO bvo) {
		log.info("backoffice_grant().....");
		return b_repository.update_backoffice_state_y(bvo.getBackoffice_no(),bvo.getBackoffice_email());
	}

	/**
	 * 마스터 메인 페이지 (호스트 가입 신청 리스트 - 거절)
	 */
	@Override
	public int backoffice_refuse(BackOfficeVO bvo) {
		return b_repository.update_backoffice_state_N(bvo.getBackoffice_no(),bvo.getBackoffice_email());
	}

	/**
	 * 마스터 - 호스트 탈퇴 신청 리스트
	 */
	@Override
	public List<BackOfficeListVO> backoffice_endList_selectAll(Integer page) { // 커버드 인덱스
		log.info("backoffice_applyList_selectAll().....");
		log.info("currentpage:{}", page);

		Integer row_count = 15;
		Integer start_row = (page - 1) * row_count + 1;
		Integer end_row = page * row_count;

		return b_list_repository.selectAll_backoffice_end(start_row, end_row);
	}

	/**
	 * 마스터 - 호스트 탈퇴 승인
	 */
	@Override
	public int backoffice_revoke(BackOfficeVO bvo) {
		return b_repository.update_backoffice_state_X(bvo.getBackoffice_no(),bvo.getBackoffice_email());
	}

	/**
	 * 백오피스 가입, 탈퇴 상세 페이지
	 */
	@Override
	public BackOfficeVO master_backoffice_detail_selectOne(BackOfficeVO bvo) {
		return b_repository.selectOne_backoffice_detail_m(bvo.getBackoffice_no());
	}
	
}
