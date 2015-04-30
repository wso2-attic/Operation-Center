/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.oc.agent.messaging.broker.connect;

import org.wso2.carbon.oc.agent.messaging.broker.connect.TopicSubscriber;
import org.wso2.carbon.oc.agent.messaging.broker.connect.amqp.AmqpTopicSubscriber;
import org.wso2.carbon.oc.agent.messaging.broker.connect.mqtt.MqttTopicSubscriber;
import org.wso2.carbon.oc.agent.messaging.broker.subscribe.MessageListener;
import org.wso2.carbon.oc.agent.messaging.util.MessagingConstants;

/**
 * Topic subscriber factory.
 */
public class TopicSubscriberFactory {

    public static TopicSubscriber createTopicSubscriber(String protocol, MessageListener messageListener, String topicName) {
        if (MessagingConstants.AMQP.equals(protocol)) {
            return new AmqpTopicSubscriber(messageListener, topicName);
        } else if (MessagingConstants.MQTT.equals(protocol)) {
            return new MqttTopicSubscriber(messageListener, topicName);
        } else {
            throw new RuntimeException("Could not create topic subscriber, unknown protocol: " + protocol);
        }
    }
}
