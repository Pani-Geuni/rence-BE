/**
 * 
 * @author 최진실
 *
 */
package com.rence.backoffice.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.rence.backoffice.model.AuthVO;
import com.rence.backoffice.model.BackOfficeOperatingTimeVO;
import com.rence.backoffice.model.BackOfficeVO;
import com.rence.backoffice.model.EmailVO;

public interface BackOfficeService {

	public BackOfficeVO insertOK(BackOfficeVO vo, BackOfficeOperatingTimeVO ovo);

	public Map<String, String> backoffice_auth(AuthVO avo, BackOfficeVO bvo, EmailVO evo);

}
