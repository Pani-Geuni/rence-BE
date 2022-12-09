/**
 * @author 최진실
 */
package com.rence.master.service;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.rence.backoffice.model.BackOfficeVO;
import com.rence.backoffice.model.EmailVO;

public interface MasterService {

	public Map<String, Object> backoffice_applyList_selectAll(Integer page);

	public Map<String, String> master_grant(BackOfficeVO bvo, EmailVO evo) throws UnsupportedEncodingException;

	public Map<String, String> master_refuse(BackOfficeVO bvo, EmailVO evo);

	public Map<String, Object> backoffice_end(Integer page);

	public Map<String, String> master_revoke(BackOfficeVO bvo, EmailVO evo);

	public Map<String, Object> master_backoffice_detail_selectOne(BackOfficeVO bvo, String page);

}
