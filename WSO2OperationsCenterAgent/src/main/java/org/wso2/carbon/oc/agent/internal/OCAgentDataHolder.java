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
import org.wso2.carbon.base.api.ServerConfigurationService;
import org.wso2.carbon.oc.agent.artifact.extractor.OCArtifactProvider;
import org.wso2.carbon.server.admin.common.IServerAdmin;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.utils.ConfigurationContextService;

import java.util.ArrayList;
import java.util.List;

public class OCAgentDataHolder {
	private static final Log logger = LogFactory.getLog(OCAgentDataHolder.class);
	private static OCAgentDataHolder instance = new OCAgentDataHolder();
	private ConfigurationContextService configurationContextService;
	private ServerConfigurationService serverConfigurationService;
	private IServerAdmin serverAdmin;
	private RealmService realmService;
	private int serverId;
	private List<OCArtifactProvider> ocArtifactProviders =
			new ArrayList<OCArtifactProvider>();

	private OCAgentDataHolder() {
	    /* No initializations needed for the moment. */
	}

	public static OCAgentDataHolder getInstance() {
		return instance;
	}

	public ConfigurationContextService getConfigurationContextService() {
		return configurationContextService;
	}

	public void setConfigurationContextService(
			ConfigurationContextService configurationContextService) {
		this.configurationContextService = configurationContextService;
	}

	public ServerConfigurationService getServerConfigurationService() {
		return serverConfigurationService;
	}

	public void setServerConfigurationService(
			ServerConfigurationService serverConfigurationService) {
		this.serverConfigurationService = serverConfigurationService;
	}

	public IServerAdmin getServerAdmin() {
		return serverAdmin;
	}

	public void setServerAdmin(IServerAdmin serverAdmin) {
		this.serverAdmin = serverAdmin;
	}

	public RealmService getRealmService() {
		return realmService;
	}

	public void setRealmService(RealmService realmService) {
		this.realmService = realmService;
	}

	public List<OCArtifactProvider> getOcArtifactProviders() {
		return ocArtifactProviders;
	}

	public void setOcArtifactProviders(OCArtifactProvider ocArtifactProvider) {
		ocArtifactProviders.add(ocArtifactProvider);
	}
}
