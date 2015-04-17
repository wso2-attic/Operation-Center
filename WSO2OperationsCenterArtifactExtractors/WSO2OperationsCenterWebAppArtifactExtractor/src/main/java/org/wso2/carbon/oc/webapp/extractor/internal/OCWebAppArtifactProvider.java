/*
 * Copyright 2015 The Apache Software Foundation.
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

package org.wso2.carbon.oc.webapp.extractor.internal;

import org.apache.axis2.context.ConfigurationContext;
import org.wso2.carbon.oc.agent.artifact.extractor.OCArtifactProvider;
import org.wso2.carbon.oc.agent.beans.OCArtifact;
import org.wso2.carbon.oc.webapp.extractor.OCWebAppDataHolder;
import org.wso2.carbon.oc.webapp.extractor.OCWebAppExtractorConstants;
import org.wso2.carbon.webapp.mgt.WebApplication;
import org.wso2.carbon.webapp.mgt.WebApplicationsHolder;

import java.util.*;

public class OCWebAppArtifactProvider implements OCArtifactProvider {

	@Override
	public List<OCArtifact> getArtifacts() {

		if (OCWebAppDataHolder.getInstance().getConfigurationContextService() == null) {
			return new ArrayList<OCArtifact>();
		}

		ConfigurationContext serverConfigurationContext =
				OCWebAppDataHolder.getInstance().getConfigurationContextService()
				                  .getServerConfigContext();
		WebApplicationsHolder webApplicationsHolder =
				(WebApplicationsHolder) ((HashMap) serverConfigurationContext
						.getProperty(OCWebAppExtractorConstants.HOLDER_LIST_PROPERTY))
						.get(OCWebAppExtractorConstants.WEB_APPS);
		Map<String, WebApplication> startedWebAppMap = webApplicationsHolder.getStartedWebapps();
		Map<String, WebApplication> faultyWebAppMap = webApplicationsHolder.getFaultyWebapps();
		Map<String, WebApplication> stoppedWebAppMap = webApplicationsHolder.getStoppedWebapps();

		List<OCArtifact> webApps = new ArrayList<OCArtifact>();

		addWebAppsToList(webApps,startedWebAppMap);
		addWebAppsToList(webApps,faultyWebAppMap);
		addWebAppsToList(webApps,stoppedWebAppMap);

		return webApps;
	}

	public void addWebAppsToList(List<OCArtifact> webApps, Map<String, WebApplication> webAppMap){
		for (WebApplication webApplication : webAppMap.values()) {
			OCArtifact artifact = new OCArtifact(webApplication.getLastModifiedTime(),
			                                     webApplication.getVersion());
			artifact.setProperty(OCWebAppExtractorConstants.DISPLAY_NAME,
			                     webApplication.getDisplayName());
			artifact.setProperty(OCWebAppExtractorConstants.WEB_APP_TYPE,
			                     OCWebAppExtractorConstants.WEB_APP_FILTER);
			artifact.setProperty(OCWebAppExtractorConstants.CONTEXT,
			                     webApplication.getContextName());
			artifact.setProperty(OCWebAppExtractorConstants.HOST_NAME,
			                     webApplication.getHostName());
			artifact.setProperty(OCWebAppExtractorConstants.STATE, webApplication.getState());
			webApps.add(artifact);
		}
	}

	@Override
	public String getArtifactType() {
		return OCWebAppExtractorConstants.ARTIFACT_TYPE;
	}
}