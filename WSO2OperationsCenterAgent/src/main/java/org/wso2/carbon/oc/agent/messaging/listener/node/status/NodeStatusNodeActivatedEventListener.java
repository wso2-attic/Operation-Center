package org.wso2.carbon.oc.agent.messaging.listener.node.status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.oc.agent.messaging.broker.subscribe.MessageListener;
import org.wso2.carbon.oc.agent.messaging.domain.Message;

/**
 * Created by noelyahan on 4/30/15.
 */
public class NodeStatusNodeActivatedEventListener implements MessageListener{
	private static final Log log = LogFactory.getLog(NodeStatusNodeActivatedEventListener.class);
	@Override
	public void messageReceived(Message message) {
		log.info(">> NodeStatusNodeActivatedEventListener start");
		log.info(message.getTopicName());
		log.info(message.getText());
		log.info(">> NodeStatusNodeActivatedEventListener end");
		log.info("\n\n\n\n");
	}
}
