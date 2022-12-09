/**
 * @author 최진실
 */
package com.rence.master.repository;

import java.util.List;

import com.rence.backoffice.model.BackOfficeListVO;
import com.rence.backoffice.model.BackOfficeVO;

public interface MasterDAO {

	public List<BackOfficeListVO> backoffice_applyList_selectAll(Integer page);

	public int backoffice_grant(BackOfficeVO bvo);

	public int backoffice_refuse(BackOfficeVO bvo);

	public List<BackOfficeListVO> backoffice_endList_selectAll(Integer page);

	public int backoffice_revoke(BackOfficeVO bvo);

	public BackOfficeVO master_backoffice_detail_selectOne(BackOfficeVO bvo);

}
