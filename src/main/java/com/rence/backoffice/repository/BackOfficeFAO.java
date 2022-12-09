package com.rence.backoffice.repository;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.rence.backoffice.model.BackOfficeVO;

public interface BackOfficeFAO {

	public BackOfficeVO backoffice_image_upload(BackOfficeVO vo, MultipartHttpServletRequest mtfRequest,
			MultipartFile multipartFile_room);

	public BackOfficeVO host_image_upload(BackOfficeVO vo, MultipartFile multipartFile_host);


}
