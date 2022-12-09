/**
 * 
 * @author 최진실
 *
 */
package com.rence.backoffice.repository;

import com.rence.backoffice.model.AuthVO;
import com.rence.backoffice.model.BackOfficeOperatingTimeVO;
import com.rence.backoffice.model.BackOfficeVO;

public interface BackOfficeDAO {

	public BackOfficeVO backoffice_insertOK(BackOfficeVO vo);

	public int backoffice_operating_insertOK(BackOfficeOperatingTimeVO ovo);

	public BackOfficeVO backoffice_email_check(BackOfficeVO bvo);

	public AuthVO backoffice_auth_overlap(BackOfficeVO bvo);

	public AuthVO backoffice_auth_insert(AuthVO avo);

	public AuthVO backoffice_authOK_select(String backoffice_email, String auth_code);

	public void backoffice_auth_delete(AuthVO avo2);

	public BackOfficeVO backoffice_login_info(String username);

	public BackOfficeVO backoffice_id_email_select(BackOfficeVO bvo);

	public int backoffice_resetOK_pw(BackOfficeVO bvo2);

	public int backoffice_settingOK_pw(BackOfficeVO bvo);

}