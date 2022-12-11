/**
 * 
 * @author 최진실
 *
 */
package com.rence.backoffice.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.rence.backoffice.model.AuthVO;
import com.rence.backoffice.model.BackOfficeOperatingTimeVO;
import com.rence.backoffice.model.BackOfficeVO;
import com.rence.backoffice.model.EmailVO;

public interface BackOfficeService {

	public Map<String, String> insertOK(BackOfficeVO vo, BackOfficeOperatingTimeVO ovo);

	public Map<String, String> backoffice_auth(AuthVO avo, BackOfficeVO bvo, EmailVO evo);

	public Map<String, String> backoffice_authOK(AuthVO avo, String backoffice_email, String auth_code);

	public Map<String, String> backoffice_loginOK(String username, HttpServletResponse response, HttpSession session);

	public Map<String, String> backoffice_reset_pw(BackOfficeVO bvo, EmailVO evo);

	public Map<String, String> backoffice_settingOK_pw(BackOfficeVO bvo, HttpServletRequest request,
			HttpServletResponse response);

	public void auth_auto_delete(String user_email);

}
