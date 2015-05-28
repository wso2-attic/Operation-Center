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

package org.wso2.carbon.oc.pubsub.messaging.message.processor;

import org.wso2.carbon.oc.pubsub.messaging.event.EventObservable;

/**
 * Message processor definition.
 */
public abstract class MessageProcessor extends EventObservable {

    /**
     * Link a message processor and its successor, if there's any.
     *
     * @param nextProcessor
     */
    public abstract void setNext(MessageProcessor nextProcessor);

    /**
     * Message processing and delegating logic.
     *
     * @param type    type of the message.
     * @param message real message body.
     * @param object  Object that will get updated.
     * @return whether the processing was successful or not.
     */
    public abstract boolean process(String type, String message, Object object);
}