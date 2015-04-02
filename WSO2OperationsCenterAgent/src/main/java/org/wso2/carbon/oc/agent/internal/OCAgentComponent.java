/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.oc.agent.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.base.api.ServerConfigurationService;
import org.wso2.carbon.oc.agent.artifact.extractor.OCArtifactProvider;
import org.wso2.carbon.oc.agent.model.OCPublisherConfiguration;
import org.wso2.carbon.oc.agent.publisher.OCDataPublisher;
import org.wso2.carbon.server.admin.common.IServerAdmin;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.utils.ConfigurationContextService;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @scr.component name="org.wso2.carbon.oc.operationscenteragentcomponent" immediate="true"
 * @scr.reference name="config.context.service"
 * interface="org.wso2.carbon.utils.ConfigurationContextService"
 * cardinality="1..1" policy="dynamic"
 * bind="setConfigurationContextService"
 * unbind="unsetConfigurationContextService"
 * @scr.reference name="server.configuration"
 * interface="org.wso2.carbon.base.api.ServerConfigurationService"
 * cardinality="1..1" policy="dynamic"
 * bind="setServerConfigurationService"
 * unbind="unsetServerConfigurationService"
 * @scr.reference name="org.wso2.carbon.server.admin.common"
 * interface="org.wso2.carbon.server.admin.common.IServerAdmin"
 * cardinality="1..1" policy="dynamic"
 * bind="setServerAdminService"
 * unbind="unsetServerAdminService"
 * @scr.reference name="org.wso2.carbon.user.core.service"
 * interface="org.wso2.carbon.user.core.service.RealmService"
 * cardinality="0..1" policy="dynamic"
 * bind="setRealmService"
 * unbind="unsetRealmService"
 * @scr.reference name="org.wso2.carbon.oc.artifact.extractor"
 * interface="org.wso2.carbon.oc.agent.artifact.extractor.OCArtifact"
 * cardinality="0..n" policy="dynamic"
 * bind="setArtifactService"
 * unbind="unsetArtifactService"
 */

public class OCAgentComponent {
	private static final ScheduledExecutorService reporterTaskExecutor =
			Executors.newScheduledThreadPool(1);
	private static final Log logger = LogFactory.getLog(OCAgentComponent.class);

	protected void activate(ComponentContext componentContext) {
		try {
			logger.info("Activating Operations Center Agent component.");

			List<OCPublisherConfiguration> ocPublisherConfigurationList =
					OCAgentUtils.getOcPublishers().getPublishersList();

			//active publisher config map
			for (OCPublisherConfiguration ocPublisherConfiguration : ocPublisherConfigurationList) {
				OCDataPublisher ocDataPublisher = null;

				Class publisherClass = Class.forName(ocPublisherConfiguration.getClassPath());

				ocDataPublisher = (OCDataPublisher) publisherClass.newInstance();

				ocDataPublisher.init(ocPublisherConfiguration);

				OCAgentReporterTask ocAgentReporterTask
						= new OCAgentReporterTask(ocDataPublisher);

				reporterTaskExecutor.scheduleAtFixedRate(ocAgentReporterTask,
				                                         0,
				                                         ocDataPublisher.getInterval(),
				                                         TimeUnit.MILLISECONDS);
			}

		} catch (Throwable throwable) {
			logger.error("Failed to activate OperationsCenterAgentComponent", throwable);
			reporterTaskExecutor.shutdown();
		}

	}

	protected void deactivate(ComponentContext componentContext) {
		logger.info("Deactivating Operations Center Agent component.");
		reporterTaskExecutor.shutdown();
	}

	protected void unsetConfigurationContextService(
			ConfigurationContextService configurationContextService) {
		OCAgentDataHolder.getInstance().setConfigurationContextService(null);
	}

	protected void setConfigurationContextService(
			ConfigurationContextService configurationContextService) {
		OCAgentDataHolder.getInstance()
		                 .setConfigurationContextService(configurationContextService);
	}

	protected void unsetServerConfigurationService(
			ServerConfigurationService serverConfigurationService) {
		OCAgentDataHolder.getInstance().setServerConfigurationService(null);
	}

	protected void setServerConfigurationService(
			ServerConfigurationService serverConfigurationService) {
		OCAgentDataHolder.getInstance()
		                 .setServerConfigurationService(serverConfigurationService);
	}

	protected void unsetServerAdminService(IServerAdmin serverAdmin) {
		OCAgentDataHolder.getInstance().setServerAdmin(null);
	}

	protected void setServerAdminService(IServerAdmin serverAdmin) {
		OCAgentDataHolder.getInstance().setServerAdmin(serverAdmin);
	}

	protected void setRealmService(RealmService realmService) {
		OCAgentDataHolder.getInstance().setRealmService(realmService);
	}

	protected void unsetRealmService(RealmService realmService) {
		OCAgentDataHolder.getInstance().setRealmService(null);
	}

	protected void setArtifactService(
			OCArtifactProvider ocArtifactProvider) {
		OCAgentDataHolder.getInstance().setOcArtifactProviders(ocArtifactProvider);
	}

	protected void unsetArtifactService(
			OCArtifactProvider ocArtifactProvider) {
		OCAgentDataHolder.getInstance().setOcArtifactProviders(null);
	}

}
