package primefaces.spring.web.mbeans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import com.techdazzler.spring.service.AbstractService;




@Named("beanController")
public class BeanController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	AbstractService abstractService;

	@Inject
	AbstractService triggerRule;
	
	
	
	public BeanController() {
		System.out.println("Getting inside to call Beans Controllers");
		
		
	}
	@PostConstruct
	public void init(){
		
		abstractService.configureKnowledgeBase();
	}
	
	public void triggerRuleService(){
		triggerRule.triggerRules();
		
	}

	
	
	
}
