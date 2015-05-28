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

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.oc.pubsub.controller.beans.OCCommand;
import org.wso2.carbon.oc.pubsub.controller.beans.OCMessage;
import org.wso2.carbon.oc.pubsub.controller.beans.OCMessageProvider;
import org.wso2.carbon.oc.pubsub.controller.beans.Test;
import org.wso2.carbon.oc.pubsub.messaging.broker.subscribe.MessageListener;
import org.wso2.carbon.oc.pubsub.messaging.domain.Message;

/**
 * Update and hold oc message and oc component
 */
public class OCMessageHolder implements OCMessageProvider, MessageListener {
	private OCMessage ocMessage;
	private OCCommand ocCommand;
	private Gson gson = new Gson();
	private static final Log log = LogFactory.getLog(OCMessageProvider.class);
	private OCMessageProvider ocMessageProvider;

	public OCMessageHolder(){}
	public OCMessageHolder(OCMessageProvider ocMessageProvider) {
		this.ocMessageProvider = ocMessageProvider;
	}

	@Override public void setOCMessage(OCMessage ocMessage) {
		this.ocMessage = ocMessage;
	}

	@Override public OCMessage getOCMessage() {
		return ocMessage;
	}

	@Override public void setOCCommand(OCCommand ocCommand) {
		this.ocCommand = ocCommand;
	}

	@Override public OCCommand getOCCommand() {
		return ocCommand;
	}

	@Override public void messageReceived(Message message) {
		Test test = gson.fromJson(
				message.getText(),
				Test.class );
		ocMessage = test.getOcMessage();
		ocMessageProvider.setOCMessage(test.getOcMessage());

//		log.info("+++++++++++");
//		if(ocMessageProvider.getOCCommand() != null){
//			log.info(ocMessageProvider.getOCCommand().getCommandName());
//			log.info(ocMessageProvider.getOCCommand().getNodeId());
//		}

//		log.info(ocMessage.getServerName());
	}
}
