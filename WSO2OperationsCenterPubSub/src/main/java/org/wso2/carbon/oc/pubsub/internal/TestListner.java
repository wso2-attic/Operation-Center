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
import org.wso2.carbon.oc.pubsub.controller.beans.Test;
import org.wso2.carbon.oc.pubsub.messaging.broker.subscribe.MessageListener;
import org.wso2.carbon.oc.pubsub.messaging.domain.Message;

/**
 * Created by noelyahan on 5/11/15.
 */
public class TestListner implements MessageListener {
	private static final Log log = LogFactory.getLog(TestListner.class);
	private Gson gson = new Gson();

	@Override
	public void messageReceived(Message message) {
//		log.info(">> NodeStatusNodeActivatedEventListener start");
//		log.info(message.getTopicName());
//		log.info(message.getText());
//		log.info(">> NodeStatusNodeActivatedEventListener end");




		Test test = gson.fromJson(
				message.getText(),
				Test.class );
		OCMessageHolder ocMessageHolder = new OCMessageHolder();
		ocMessageHolder.setOCMessage(test.getOcMessage());
//		log.info(test.getOcMessage().getAdminServiceUrl());
//		log.info(test.getOcMessage().getServerName());
		DataHolder.getInstance().setOcMessageHolder(ocMessageHolder);
//		log.info(element.getOcMessage().getServerName());
//		log.info(element.getOcMessage().getAdminServiceUrl());
//		log.info(element.getServerName());
//		log.info(element.getAdminServiceUrl());
//		log.info(element.getLocalIp());
		log.info(test.getOcMessage().getAdminServiceUrl());
	}
}
