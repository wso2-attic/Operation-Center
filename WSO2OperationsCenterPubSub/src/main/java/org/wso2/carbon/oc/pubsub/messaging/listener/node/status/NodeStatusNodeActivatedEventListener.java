/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.oc.pubsub.messaging.listener.node.status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.oc.pubsub.messaging.broker.subscribe.MessageListener;
import org.wso2.carbon.oc.pubsub.messaging.domain.Message;
import org.wso2.carbon.oc.pubsub.messaging.event.Event;
import org.wso2.carbon.oc.pubsub.messaging.event.node.notifier.NodeNotifierCommandEvent;
import org.wso2.carbon.oc.pubsub.messaging.event.node.status.NodeStatusNodeActivatedEvent;
import org.wso2.carbon.oc.pubsub.messaging.listener.EventListener;

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
