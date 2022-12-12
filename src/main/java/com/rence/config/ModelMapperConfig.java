/**
 * @author 강경석
 * //참고 출처
 * //https://velog.io/@kimview/ModelMapper
 * //https://oingdaddy.tistory.com/350
 * //map 메소드 인자의 순서는 source, target
 * ~를(sourcee) ~로((sourcee))
 */


package com.rence.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

	
	@Bean
	public ModelMapper modelMapper() {
	    return new ModelMapper();
	}
}//end class
