package pl.edu.agh.security.bpmn.main;

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

import pl.edu.agh.security.bpmn.work.items.AuthenticateWorkItemHandler;
import pl.edu.agh.security.bpmn.work.items.RegisterFinancialTransactionWorkItemHandler;
import pl.edu.agh.security.bpmn.work.items.StoreStateRequestWorkItemHandler;

/**
 * This is a sample file to launch a process.
 */
public class ProcessMain {

	public static final void main(String[] args) throws Exception {
		// load up the knowledge base
		KnowledgeBase kbase = readKnowledgeBase();
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		ksession.getWorkItemManager().registerWorkItemHandler("Authenticate",
				new AuthenticateWorkItemHandler());
		ksession.getWorkItemManager().registerWorkItemHandler(
				"StoresStateRequest", new StoreStateRequestWorkItemHandler());
		ksession.getWorkItemManager().registerWorkItemHandler(
				"RegisterFinancialTransaction",
				new RegisterFinancialTransactionWorkItemHandler());
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
				System.out.println("Koniec");
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
		values.put("count", 5);
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