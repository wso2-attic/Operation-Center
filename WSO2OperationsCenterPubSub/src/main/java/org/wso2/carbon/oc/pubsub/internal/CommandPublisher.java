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
import org.wso2.carbon.oc.pubsub.controller.beans.OCMessage;
import org.wso2.carbon.oc.pubsub.controller.beans.OCMessageProvider;

/**
 * Publish command event to message broker
 */
public class CommandPublisher implements Runnable{
	OCMessageProvider ocMessageProvider;
	private static final Log log = LogFactory.getLog(CommandPublisher.class);
	public CommandPublisher(OCMessageHolder ocMessageHolder) {
		ocMessageProvider = ocMessageHolder;
	}

	@Override public void run() {
		log.info("ocCommand");
		if(ocMessageProvider.getOCCommand() != null) {
			log.info(ocMessageProvider.getOCCommand().getNodeId());
			log.info(ocMessageProvider.getOCCommand().getCommandName());
//			NodeEventPublisher.sendNodeCommandEvent(ocMessageProvider.getOCCommand(), "node.Command");
		}else{
			log.info("OCCOmmand is null");
		}
//		log.info(ocMessage.getCommand());
//		NodeEventPublisher.sendNodeStatusNodeTerminatingEvent(ocMessage, "node.Commands");
	}
}
