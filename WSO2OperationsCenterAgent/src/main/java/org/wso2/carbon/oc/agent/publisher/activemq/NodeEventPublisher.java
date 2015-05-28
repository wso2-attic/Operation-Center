package org.wso2.carbon.oc.agent.publisher.activemq;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.oc.agent.beans.OCMessage;
import org.wso2.carbon.oc.agent.messaging.broker.publish.EventPublisher;
import org.wso2.carbon.oc.agent.messaging.broker.publish.EventPublisherPool;
import org.wso2.carbon.oc.agent.messaging.event.Event;
import org.wso2.carbon.oc.agent.messaging.event.node.status.NodeStatusNodeActivatedEvent;
import org.wso2.carbon.oc.agent.messaging.util.MessagingUtil;

/**
 *
 */
public class NodeEventPublisher {
	private static final Log log = LogFactory.getLog(NodeEventPublisher.class);
	public static void sendNodeStatusNodeActivatedEvent(OCMessage ocMessage) {
		Event nodeStatusNodeActivatedEvent =
				new NodeStatusNodeActivatedEvent(ocMessage);
		publishEvent(nodeStatusNodeActivatedEvent);
	}
	public static void sendNodeStatusNodeTerminatingEvent() {}
	public static void sendNodeStatusNodeTerminatedEvent() {}


	public static void publishEvent(Event event) {
		//publishing events to application status topic
		String topic = MessagingUtil.getMessageTopicName(event);
//		log.info("Event published: ");
//		log.info(topic);
		EventPublisher eventPublisher = EventPublisherPool.getPublisher(topic);
		eventPublisher.publish(event);
	}
}
