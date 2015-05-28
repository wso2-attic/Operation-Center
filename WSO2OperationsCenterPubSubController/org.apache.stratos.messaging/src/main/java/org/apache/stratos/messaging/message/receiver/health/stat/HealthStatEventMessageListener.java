/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.stratos.messaging.message.receiver.health.stat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.stratos.messaging.broker.subscribe.MessageListener;
import org.apache.stratos.messaging.domain.Message;

/**
 * Implements functionality for receiving text based event messages from the
 * health stat
 * message broker topic and add them to the event queue.
 */
public class HealthStatEventMessageListener implements MessageListener {

    private static final Log log = LogFactory.getLog(HealthStatEventMessageListener.class);

    private final HealthStatEventMessageQueue messageQueue;

    public HealthStatEventMessageListener(HealthStatEventMessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    @Override
    public void messageReceived(Message message) {
        try {
            if (log.isDebugEnabled()) {
                log.debug(String.format("Health stat event message received: %s",
                        message.getText()));
            }
            // Add received message to the queue
            messageQueue.add(message);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
