package com.rence.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rence.user.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Object> {
	
	//spring security - 회원정보
	@Query(nativeQuery = true, value = "select * from userinfo where user_id=?1")
	UserEntity findByUser_email(String user_id);
	
	

	
}// end class
