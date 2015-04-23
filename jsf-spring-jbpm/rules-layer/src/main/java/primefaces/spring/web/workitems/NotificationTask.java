package primefaces.spring.web.workitems;

import java.util.HashMap;

import org.drools.process.instance.WorkItemHandler;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemManager;
import org.springframework.stereotype.Component;



@Component
public class NotificationTask implements WorkItemHandler {
	
	public NotificationTask() {
		System.out.println("inside Notification Handler");
	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		
		
	}

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		HashMap<String, Object> results = new HashMap<String, Object>();
		String statusKey=(String)workItem.getParameter("statusKey");
		System.out.println("Performing Action on Status Key:"+statusKey);
		results.put("result",statusKey);
		manager.completeWorkItem(workItem.getId(),results);
	}

}
