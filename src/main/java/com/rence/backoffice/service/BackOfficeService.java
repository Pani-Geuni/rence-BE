package com.rence.backoffice.service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.rence.backoffice.model.BackOfficeOperatingTimeVO;
import com.rence.backoffice.model.BackOfficeVO;

public interface BackOfficeService {

	public BackOfficeVO insertOK(BackOfficeVO vo, BackOfficeOperatingTimeVO ovo);

}
