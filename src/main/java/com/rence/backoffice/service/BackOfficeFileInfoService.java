/**
 * 
 * @author 최진실
 *
 */
package com.rence.backoffice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.rence.backoffice.model.BackOfficeVO;
import com.rence.backoffice.repository.BackOfficeFAO;

@Service
public class BackOfficeFileInfoService implements BackOfficeFileService {
	
	@Autowired
	BackOfficeFAO fao;

	@Override
	public BackOfficeVO backoffice_image_upload(BackOfficeVO vo, MultipartHttpServletRequest mtfRequest,
			MultipartFile multipartFile_room, MultipartFile multipartFile_host) {
		vo = fao.backoffice_image_upload(vo, mtfRequest, multipartFile_room);
		vo = fao.host_image_upload(vo, multipartFile_host);
		return vo;
	}

}
