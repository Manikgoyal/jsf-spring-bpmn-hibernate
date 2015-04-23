package primefaces.spring.web.rules;

import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import org.drools.runtime.StatefulKnowledgeSession;
import org.jbpm.task.TaskService;
import org.springframework.stereotype.Service;
@Service
public class JbpmUtilImpl implements JbpmUtil {

	public void init(){
		
		try {
			UserTransaction ut = (UserTransaction) new InitialContext().lookup( "java:comp/UserTransaction" );
	            ut.begin();
	            StatefulKnowledgeSession ksession = JbpmAPIUtil.getSession();
	            TaskService taskService = JbpmAPIUtil.getTaskService();
	            System.setProperty("jbpm.usergroup.callback", "org.jbpm.task.service.DefaultUserGroupCallbackImpl");
	            ut.commit();
			} catch (Throwable t) {
				System.out.println(t.getMessage() +""+ t.getCause());
				throw new RuntimeException("error while creating session",t);
			}
		
		
	}
	
	
}
