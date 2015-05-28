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

/**
 * This is for subscribe/publish oc data from message broker topics
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.oc.pubsub.controller.beans.OCMessageProvider;
import org.wso2.carbon.oc.pubsub.messaging.broker.subscribe.EventSubscriber;
import org.wso2.carbon.utils.ConfigurationContextService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @scr.component name="org.wso2.carbon.oc.pubsub" immediate="true"
 * @scr.reference name="config.context.service"
 * interface="org.wso2.carbon.utils.ConfigurationContextService"
 * cardinality="1..1" policy="dynamic"
 * bind="setConfigurationContextService"
 * unbind="unsetConfigurationContextService"
 */
public class OCPubSubComponent {
	private static final Log log = LogFactory.getLog(OCPubSubComponent.class);
	private ExecutorService executorService;
	private EventSubscriber eventSubscriber;
	private TestListner testListner;
	private static final ScheduledExecutorService reporterTaskExecutor =
			Executors.newScheduledThreadPool(1);


	private final static String TOPIC_NAME = "node.status.NodeStatusNodeActivatedEvent";


	protected void activate(ComponentContext componentContext) {
		testListner = new TestListner();

		executorService = Executors.newFixedThreadPool(1);

		OCMessageProvider ocMessageProvider = new org.wso2.carbon.oc.pubsub.controller.beans.OCMessageHolder();

		componentContext.getBundleContext().registerService(OCMessageProvider.class, ocMessageProvider,null);
		OCMessageHolder ocMessageHolder = new OCMessageHolder(ocMessageProvider);
		eventSubscriber = new EventSubscriber(TOPIC_NAME, ocMessageHolder);


//new CommandPublisher(ocMessageHolder.getOCMessage())
		reporterTaskExecutor.scheduleAtFixedRate(new CommandPublisher(ocMessageHolder),
		                                         0,
		                                         5000,
		                                         TimeUnit.MILLISECONDS);


		reporterTaskExecutor.scheduleAtFixedRate(eventSubscriber,
		                                         0,
		                                         5000,
		                                         TimeUnit.MILLISECONDS);
		log.info("Activated PUB-SUB Component");
	}



	protected void deactivate(ComponentContext componentContext) {
		log.info("Deactivated PUB-SUB Component");
	}

	protected void unsetConfigurationContextService(
			ConfigurationContextService configurationContextService) {
	}

	protected void setConfigurationContextService(
			ConfigurationContextService configurationContextService) {
	}
}
