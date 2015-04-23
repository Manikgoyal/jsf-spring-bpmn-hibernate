package primefaces.spring.web.rules;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.SystemEventListenerFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.impl.EnvironmentFactory;
import org.drools.io.ResourceFactory;
import org.drools.persistence.jpa.JPAKnowledgeService;
import org.drools.runtime.Environment;
import org.drools.runtime.EnvironmentName;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.jbpm.process.audit.JPAProcessInstanceDbLog;
import org.jbpm.process.audit.JPAWorkingMemoryDbLogger;
import org.jbpm.process.workitem.wsht.LocalHTWorkItemHandler;
import org.jbpm.task.TaskService;
import org.jbpm.task.service.TaskServiceSession;
import org.jbpm.task.service.local.LocalTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import primefaces.spring.web.workitems.NotificationTask;
import bitronix.tm.TransactionManagerServices;

@Component
public class JbpmAPIUtil {

	static Logger logger = Logger.getLogger(JbpmAPIUtil.class);

	
	public static TaskService taskService;
	public static StatefulKnowledgeSession ksession;
	public static EntityManagerFactory emf;

	public static StatefulKnowledgeSession getSession() {

		if (emf == null) {
			emf = Persistence
					.createEntityManagerFactory("org.jbpm.persistence.jpa");
		}

		if (ksession == null) {
			ksession = JbpmAPIUtil.createKnowledgeSession(emf);
		}
	
		return ksession;

	}

	public static StatefulKnowledgeSession createKnowledgeSession(
			EntityManagerFactory emf) {
		KnowledgeBase kbase = createKnowledgeBase();
		return createKnowledgeSession(kbase, emf);
	}

	public static StatefulKnowledgeSession createKnowledgeSession(
			KnowledgeBase kbase, EntityManagerFactory emf) {
		StatefulKnowledgeSession result;
		final KnowledgeSessionConfiguration conf = KnowledgeBaseFactory
				.newKnowledgeSessionConfiguration();

		Environment env = createEnvironment(emf);
		result = JPAKnowledgeService.newStatefulKnowledgeSession(kbase, conf,
				env);
		new JPAWorkingMemoryDbLogger(result);

		JPAProcessInstanceDbLog log = new JPAProcessInstanceDbLog(
				result.getEnvironment());
		return result;
	}

	public static EntityManagerFactory getEmf(EntityManagerFactory emf) {
		emf = Persistence
				.createEntityManagerFactory("org.jbpm.persistence.jpa");
		return emf;
	}

	protected static Environment createEnvironment(EntityManagerFactory emf) {
		Environment env = EnvironmentFactory.newEnvironment();
		env.set(EnvironmentName.ENTITY_MANAGER_FACTORY, emf);
		env.set(EnvironmentName.TRANSACTION_MANAGER,
				TransactionManagerServices.getTransactionManager());
		return env;
	}

	public static KnowledgeBase createKnowledgeBase() {

		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
				.newKnowledgeBuilder();
		System.out.println("*************************1");
		kbuilder.add(ResourceFactory.newClassPathResource("META-INF/rule-set.xml"),
				ResourceType.CHANGE_SET);
		/*kbuilder.add(ResourceFactory.newClassPathResource("SampleHumanTaskFormVariables.bpmn"), ResourceType.BPMN2);*/
		System.out.println("*************************5");
		if (kbuilder.hasErrors()) {
			System.out.println("*************************2");
			if (kbuilder.getErrors().size() > 0) {
				boolean errors = false;
				System.out.println("*************************3");
				for (KnowledgeBuilderError error : kbuilder.getErrors()) {
					logger.warn(error.toString());
					System.out.println("*************************4");
					errors = true;
				}

			}
		}
		return kbuilder.newKnowledgeBase();

	}

	public static TaskService getTaskService() throws Exception {

		if (taskService == null) {
			if (emf == null)
				emf = Persistence
						.createEntityManagerFactory("org.jbpm.persistence.jpa");

			org.jbpm.task.service.TaskService tservice = getService(emf);
			if (ksession == null) {
				ksession = getSession();
			}
			taskService = getTskService(ksession, tservice, emf);
		}

		return taskService;
	}

	public static TaskService getTskService(StatefulKnowledgeSession ksession,
			org.jbpm.task.service.TaskService taskService,
			EntityManagerFactory emf) {
		if (taskService == null) {
			taskService = new org.jbpm.task.service.TaskService(emf,
					SystemEventListenerFactory.getSystemEventListener());
		}
		TaskServiceSession taskServiceSession = taskService.createSession();
		
		
		LocalHTWorkItemHandler humanTaskHandler = new LocalHTWorkItemHandler(new LocalTaskService(taskService),ksession);
		humanTaskHandler.setLocal(true);
		humanTaskHandler.connect();
		ksession.getWorkItemManager().registerWorkItemHandler("Human Task",
				humanTaskHandler);
		return new LocalTaskService(taskService);
	}

	public static org.jbpm.task.service.TaskService getService(
			EntityManagerFactory emf) {
		return new org.jbpm.task.service.TaskService(emf,
				SystemEventListenerFactory.getSystemEventListener());
	}
	
	
	
	
	
	public static void registerworkItemHandler(StatefulKnowledgeSession ksession){
		System.out.println("inside Ksession");
		
		
	}

}



