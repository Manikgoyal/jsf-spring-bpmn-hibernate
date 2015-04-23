package com.techdazzler.spring.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import com.techdazzler.spring.service.AbstractService;
import com.techdazzler.spring.service.AbstractServiceImpl;





@Configuration
@ComponentScan({"com.techdazzler.spring.service","primefaces.spring.web.rules","primefaces.spring.web.mbeans"})
public class AppConfig {

	
	@Bean(name="triggerRule")
	public AbstractService triggerRule(){
		AbstractServiceImpl abstractServiceImpl = new AbstractServiceImpl();
		abstractServiceImpl.setProcessName("com.sample.bpmn.sampleHTformvariables");
		return abstractServiceImpl;
	}
	
	@Bean(name="abstractService")
	public AbstractService abstractService(){
		return new AbstractServiceImpl();
	}
		
}
