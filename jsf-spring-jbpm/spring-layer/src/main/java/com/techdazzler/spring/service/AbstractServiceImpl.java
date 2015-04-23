package com.techdazzler.spring.service;

import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import org.drools.runtime.StatefulKnowledgeSession;
import org.jbpm.task.Notification;
import org.jbpm.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;





import primefaces.spring.web.rules.JbpmAPIUtil;
import primefaces.spring.web.rules.JbpmUtil;
import primefaces.spring.web.workitems.NotificationTask;


@Service
public class AbstractServiceImpl implements AbstractService{

	@Autowired
	JbpmUtil jbpmUtil;
	String processName;
	@Autowired
	NotificationTask notificationTask;
	
	@Override
	public void configureKnowledgeBase() {
		
		System.out.println("Getting Inside to check");
		jbpmUtil.init();
	}

	@Override
	public void triggerRules() {
		try {
			UserTransaction ut = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
			ut.begin();
			StatefulKnowledgeSession ksession = primefaces.spring.web.rules.JbpmAPIUtil.getSession();
			ksession.getWorkItemManager().registerWorkItemHandler("NotificationTask",notificationTask);
			TaskService taskService = primefaces.spring.web.rules.JbpmAPIUtil.getTaskService();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("Manik", "Megha");
			ksession.startProcess(processName,params);
			ksession.fireAllRules();
			ut.commit();	
		} catch (Exception e) {
			System.out.println("Error:"+ e.getMessage().toString());
        }
	}
	
	public String getProcessName() {
		return processName;
	}


	public void setProcessName(String processName) {
		this.processName = processName;
	}
	
}