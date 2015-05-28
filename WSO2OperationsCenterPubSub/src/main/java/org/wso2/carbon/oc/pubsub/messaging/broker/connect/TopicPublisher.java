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

package org.wso2.carbon.oc.pubsub.messaging.broker.connect;

import org.wso2.carbon.oc.pubsub.messaging.broker.connect.TopicConnector;

/**
 * Topic publisher interface to be implemented by a transport specific topic publisher.
 */
public interface TopicPublisher extends TopicConnector {

    /**
     * Publish a message to a topic in the message broker.
     *
     * @param message
     * @param retry
     */
    public abstract void publish(String message, boolean retry);
}
