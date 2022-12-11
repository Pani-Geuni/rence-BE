/**
 * 
 * @author 최진실
 *
 */
package com.rence.backoffice.service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.rence.backoffice.model.BackOfficeVO;

public interface BackOfficeFileService {

	public BackOfficeVO backoffice_image_upload(BackOfficeVO vo, MultipartHttpServletRequest mtfRequest,
			MultipartFile multipartFile_room, MultipartFile multipartFile_host);

	public BackOfficeVO backoffice_image_upload(BackOfficeVO bvo, MultipartHttpServletRequest mtfRequest,
			MultipartFile multipartFile_room);

}
