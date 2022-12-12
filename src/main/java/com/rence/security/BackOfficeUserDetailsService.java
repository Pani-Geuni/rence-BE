/**
 * @author 강경석
 */
package com.rence.security;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.rence.backoffice.model.BackOfficeEntity;
import com.rence.backoffice.model.BackOfficeDTO;
import com.rence.backoffice.repository.BackOfficeRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BackOfficeUserDetailsService implements UserDetailsService {
	@Autowired private BackOfficeRepository repository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Override
	public UserDetails loadUserByUsername(String backoffice_id) throws UsernameNotFoundException {
		
		BackOfficeDTO backoffice =  new BackOfficeDTO();
		
		BackOfficeEntity be = repository.findByBackoffice_email(backoffice_id);
		
		log.info("backoffice:{}", be);
		
		if (be == null) {
			throw new UsernameNotFoundException("No user found with the given email");
		} else {
			backoffice = modelMapper.map(be, BackOfficeDTO.class);
		}
		
		return new BackOfficeUserDetails(backoffice);
	}

}
