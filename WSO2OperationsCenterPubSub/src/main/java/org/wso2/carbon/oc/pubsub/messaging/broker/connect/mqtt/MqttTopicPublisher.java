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

package org.wso2.carbon.oc.pubsub.messaging.broker.connect.mqtt;

import org.wso2.carbon.oc.pubsub.messaging.broker.connect.TopicPublisher;
import org.wso2.carbon.oc.pubsub.messaging.broker.connect.mqtt.MqttTopicConnector;
import org.wso2.carbon.oc.pubsub.messaging.domain.exception.MessagingException;
import org.wso2.carbon.oc.pubsub.messaging.util.MessagingConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Mqtt topic connector
 * Usage: Create an instance and invoke connect() to connect to the message broker. Once connected invoke publish()
 * to publish messages to the given topic. When needed to disconnect invoke disconnect(), it will disconnect and
 * close the connection.
 */
public class MqttTopicPublisher extends MqttTopicConnector implements TopicPublisher {

    protected static final Log log = LogFactory.getLog(
		    org.wso2.carbon.oc.pubsub.messaging.broker.connect.mqtt.MqttTopicPublisher.class);

    private String topicName;

    public MqttTopicPublisher(String topicName) {
        this.topicName = topicName;
        create();
    }

    public void publish(String message, boolean retry) {
        try {
            if (mqttClient == null) {
                String error = "Could not publish message to topic, MQTT client has not been initialized";
                if (log.isErrorEnabled()) {
                    log.error(error);
                }
                throw new MessagingException(error);
            }

            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(MessagingConstants.QOS);
            mqttClient.publish(topicName, mqttMessage);
        } catch (Exception e) {
            String errorMessage = "Could not publish message: " + message;
            log.error(errorMessage, e);
            throw new MessagingException(errorMessage, e);
        }
    }
}
