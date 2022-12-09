package com.rence.backoffice.repository;

import com.rence.backoffice.model.BackOfficeOperatingTimeVO;
import com.rence.backoffice.model.BackOfficeOperatingTimeEntity;
import com.rence.backoffice.model.BackOfficeVO;

public interface BackOfficeDAO {

	public BackOfficeVO backoffice_insertOK(BackOfficeVO vo);

	public int backoffice_operating_insertOK(BackOfficeOperatingTimeVO ovo);

}
