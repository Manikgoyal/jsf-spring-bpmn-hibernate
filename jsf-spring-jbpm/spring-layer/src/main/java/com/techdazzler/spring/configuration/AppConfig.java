package com.techdazzler.spring.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import primefaces.spring.web.workitems.NotificationTask;

import com.techdazzler.spring.service.AbstractService;
import com.techdazzler.spring.service.AbstractServiceImpl;





@Configuration
@ComponentScan({"com.techdazzler.spring.service","primefaces.spring.web.rules","primefaces.spring.web.mbeans","primefaces.spring.web.workitems"})
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
	
	@Bean(name="notificationTask")
	@Scope("prototype")
	public NotificationTask notificationTaskWorkItemHandler(){
		return new NotificationTask();
	}
		
}
