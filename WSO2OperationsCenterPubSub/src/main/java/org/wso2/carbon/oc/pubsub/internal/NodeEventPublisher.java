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

package org.wso2.carbon.oc.pubsub.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.oc.pubsub.controller.beans.OCCommand;
import org.wso2.carbon.oc.pubsub.messaging.beans.OCMessage;
import org.wso2.carbon.oc.pubsub.messaging.broker.publish.EventPublisher;
import org.wso2.carbon.oc.pubsub.messaging.broker.publish.EventPublisherPool;
import org.wso2.carbon.oc.pubsub.messaging.event.Event;
import org.wso2.carbon.oc.pubsub.messaging.event.node.notifier.NodeNotifierCommandEvent;
import org.wso2.carbon.oc.pubsub.messaging.event.node.status.NodeStatusNodeActivatedEvent;
import org.wso2.carbon.oc.pubsub.messaging.event.node.status.NodeStatusNodeTerminatedEvent;
import org.wso2.carbon.oc.pubsub.messaging.event.node.status.NodeStatusNodeTerminatingEvent;
import org.wso2.carbon.oc.pubsub.messaging.util.MessagingUtil;

/**
 *
 */
public class NodeEventPublisher {
	private static final Log log = LogFactory.getLog(NodeEventPublisher.class);
	public static void sendNodeStatusNodeActivatedEvent(OCMessage ocMessage, String topic) {
//		Event nodeStatusNodeActivatedEvent =
//				new NodeStatusNodeActivatedEvent(ocMessage);
//		publishEvent(nodeStatusNodeActivatedEvent, topic);
	}
	public static void sendNodeStatusNodeTerminatingEvent(
			org.wso2.carbon.oc.pubsub.controller.beans.OCMessage ocMessage, String topic) {
		Event nodeStatusNodeActivatedEvent =
				new NodeStatusNodeActivatedEvent(ocMessage);
		publishEvent(nodeStatusNodeActivatedEvent, topic);
	}
	public static void sendNodeStatusNodeTerminatedEvent() {}

	public static void sendNodeCommandEvent(OCCommand ocCommand, String topic) {
		Event nodeNotifierCommandEvent =
				new NodeNotifierCommandEvent(ocCommand);
		publishEvent(nodeNotifierCommandEvent, topic);
	}


	public static void publishEvent(Event event, String topic) {
		//publishing events to application status topic
//		String topic = MessagingUtil.getMessageTopicName(event);
//		log.info("Event published: ");
//		log.info(topic);
		EventPublisher eventPublisher = EventPublisherPool.getPublisher(topic);
		eventPublisher.publish(event);
	}
}
