package org.wso2.carbon.oc.agent.messaging.listener.node.status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.oc.agent.messaging.broker.subscribe.MessageListener;
import org.wso2.carbon.oc.agent.messaging.domain.Message;
import org.wso2.carbon.oc.agent.messaging.event.Event;
import org.wso2.carbon.oc.agent.messaging.event.node.notifier.NodeNotifierCommandEvent;
import org.wso2.carbon.oc.agent.messaging.event.node.status.NodeStatusNodeActivatedEvent;
import org.wso2.carbon.oc.agent.messaging.listener.EventListener;

/**
 *
 */
public class NodeStatusNodeActivatedEventListener extends EventListener implements MessageListener{
	private static final Log log = LogFactory.getLog(NodeStatusNodeActivatedEventListener.class);

	@Override
	protected void onEvent(Event event) {
//		NodeStatusNodeActivatedEvent e = (NodeStatusNodeActivatedEvent) event;
//		e.getOcMessage();
		log.info(">> NodeStatusNodeActivatedEventListener start");
		log.info("incomming message");
		log.info(">> NodeStatusNodeActivatedEventListener end");
		log.info("\n\n\n\n");

	}
		@Override
	public void messageReceived(Message message) {
		log.info(">> NodeStatusNodeActivatedEventListener start");
		log.info(message.getTopicName());
		log.info(message.getText());
		log.info(">> NodeStatusNodeActivatedEventListener end");
		log.info("\n\n\n\n");
	}
}
