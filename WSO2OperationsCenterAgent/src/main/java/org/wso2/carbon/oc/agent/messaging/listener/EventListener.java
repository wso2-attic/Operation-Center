/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.oc.agent.messaging.listener;

import org.wso2.carbon.oc.agent.messaging.event.Event;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Observable;
import java.util.Observer;

/**
 * Event listener definition.
 */
public abstract class EventListener implements Observer {
    private static final Log log = LogFactory.getLog(
		    org.wso2.carbon.oc.agent.messaging.listener.EventListener.class);

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Event) {
            Event event = (Event) arg;
            if (log.isDebugEnabled()) {
                log.debug(String.format("Event received: %s", event.getClass().getName()));
            }
            onEvent(event);
        }
    }

    /**
     * Triggered when an event is received.
     *
     * @param event
     */
    protected abstract void onEvent(Event event);
}
