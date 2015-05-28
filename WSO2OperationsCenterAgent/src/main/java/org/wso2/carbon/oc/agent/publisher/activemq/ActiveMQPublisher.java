package org.wso2.carbon.oc.agent.publisher.activemq;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import org.wso2.carbon.oc.agent.beans.OCMessage;
import org.wso2.carbon.oc.agent.messaging.broker.connect.TopicPublisher;
import org.wso2.carbon.oc.agent.messaging.broker.connect.TopicPublisherFactory;
import org.wso2.carbon.oc.agent.messaging.broker.connect.TopicSubscriber;
import org.wso2.carbon.oc.agent.messaging.broker.connect.TopicSubscriberFactory;
import org.wso2.carbon.oc.agent.messaging.broker.subscribe.EventSubscriber;
import org.wso2.carbon.oc.agent.messaging.broker.subscribe.MessageListener;
import org.wso2.carbon.oc.agent.messaging.domain.Message;
import org.wso2.carbon.oc.agent.messaging.listener.node.status.NodeStatusNodeActivatedEventListener;
import org.wso2.carbon.oc.agent.messaging.util.MessagingUtil;
import org.wso2.carbon.oc.agent.model.OCPublisherConfiguration;
import org.wso2.carbon.oc.agent.publisher.OCDataPublisher;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class ActiveMQPublisher  implements OCDataPublisher{

	private final static String TOPIC_NAME = "node.status.NodeStatusNodeActivatedEvent";

	private static final Log log = LogFactory.getLog(ActiveMQPublisher.class);
	private long interval;

	private ExecutorService executorService;
	private EventSubscriber eventSubscriber;
	private NodeStatusNodeActivatedEventListener nodeStatusNodeActivatedEventListener;


	@Override
	public void init(OCPublisherConfiguration ocPublisherConfiguration) {
		Map<String, String> config = ocPublisherConfiguration.getOcPublisherProperties().getPropertyMap();
		this.interval = Long.parseLong(config.get(ActiveMQConstants.INTERVAL));

//		nodeStatusNodeActivatedEventListener = new NodeStatusNodeActivatedEventListener();
//
//		executorService = Executors.newFixedThreadPool(1);
//		eventSubscriber = new EventSubscriber("node.Commands", nodeStatusNodeActivatedEventListener);
//		executorService.execute(eventSubscriber);

	}

	@Override
	public void publish(OCMessage ocMessage) {

		NodeEventPublisher.sendNodeStatusNodeActivatedEvent(ocMessage);

	}

	@Override
	public long getInterval() {
		return 5000;
	}
}
