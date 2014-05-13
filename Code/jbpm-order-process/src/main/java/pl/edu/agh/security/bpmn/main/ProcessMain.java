package pl.edu.agh.security.bpmn.main;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.event.process.ProcessCompletedEvent;
import org.drools.event.process.ProcessEventListener;
import org.drools.event.process.ProcessNodeLeftEvent;
import org.drools.event.process.ProcessNodeTriggeredEvent;
import org.drools.event.process.ProcessStartedEvent;
import org.drools.event.process.ProcessVariableChangedEvent;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.jbpm.workflow.instance.WorkflowProcessInstance;

import pl.edu.agh.security.bpmn.work.items.AuthenticateWorkItemHandler;
import pl.edu.agh.security.bpmn.work.items.DeliveryWorkItemHandler;
import pl.edu.agh.security.bpmn.work.items.RegisterFinancialTransactionWorkItemHandler;
import pl.edu.agh.security.bpmn.work.items.StoreStateRequestWorkItemHandler;
import pl.edu.agh.security.bpmn.work.items.WarehouseWorkItemHandler;
import pl.edu.agh.security.warehouse.client.WarehouseResponse;

/**
 * This is a sample file to launch a process.
 */
public class ProcessMain {

	public static final void main(String[] args) throws Exception {
		System.setProperty("jsse.enableSNIExtension", "false");
		// load up the knowledge base
		KnowledgeBase kbase = readKnowledgeBase();
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		ksession.getWorkItemManager().registerWorkItemHandler("Authenticate",
				new AuthenticateWorkItemHandler());
		ksession.getWorkItemManager().registerWorkItemHandler(
				"StoresStateRequest", new StoreStateRequestWorkItemHandler());
		ksession.getWorkItemManager().registerWorkItemHandler("Delivery", new DeliveryWorkItemHandler());
		ksession.getWorkItemManager().registerWorkItemHandler(
				"RegisterFinancialTransaction",
				new RegisterFinancialTransactionWorkItemHandler());
		ksession.getWorkItemManager().registerWorkItemHandler(
				"RegisterWarehouseTransaction",
				new WarehouseWorkItemHandler());
		// start a new process instance
		ksession.addEventListener(new ProcessEventListener() {

			@Override
			public void beforeVariableChanged(ProcessVariableChangedEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeProcessStarted(ProcessStartedEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeProcessCompleted(ProcessCompletedEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeNodeTriggered(ProcessNodeTriggeredEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeNodeLeft(ProcessNodeLeftEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterVariableChanged(ProcessVariableChangedEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterProcessStarted(ProcessStartedEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterProcessCompleted(
					ProcessCompletedEvent processCompletedEvent) {
				WorkflowProcessInstance workflowProcessInstance = (WorkflowProcessInstance) processCompletedEvent
						.getProcessInstance();
				String location = (String) workflowProcessInstance
						.getVariable("location");
				String deliveryID = (String) workflowProcessInstance
                        .getVariable("deliveryID");
				Date dueDate = (Date) workflowProcessInstance
						.getVariable("dueDate");
				String invoiceIdentifier = (String) workflowProcessInstance
						.getVariable("invoiceIdentifier");
				WarehouseResponse warehouseResponse = (WarehouseResponse) workflowProcessInstance
						.getVariable("warehouseResponse");
				System.out.println("Store: " + location + ", deliveryID: " + deliveryID + ", due date: "
						+ dueDate + ", invoice number: " + invoiceIdentifier + ", warehouseResponse: "+warehouseResponse);
			}

			@Override
			public void afterNodeTriggered(ProcessNodeTriggeredEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterNodeLeft(ProcessNodeLeftEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("user", "magister");
		values.put("password", "inzynier");
		values.put("product", "woda");
		values.put("count", 3);
		values.put("invoiceRequested", true);
		ksession.startProcess("pl.edu.agh.security.bpmn.order", values);
	}

	private static KnowledgeBase readKnowledgeBase() throws Exception {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
				.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("order.bpmn"),
				ResourceType.BPMN2);
		return kbuilder.newKnowledgeBase();
	}

}