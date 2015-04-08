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

import com.jezhumble.javasysmon.JavaSysMon;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.clustering.ClusteringAgent;
import org.apache.axis2.description.Parameter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.base.api.ServerConfigurationService;
import org.wso2.carbon.oc.agent.artifact.extractor.OCArtifactProvider;
import org.wso2.carbon.oc.agent.beans.OCArtifact;
import org.wso2.carbon.oc.agent.internal.exceptions.ParameterUnavailableException;
import org.wso2.carbon.oc.agent.message.OCMessage;
import org.wso2.carbon.server.admin.common.ServerUpTime;
import org.wso2.carbon.server.admin.service.ServerAdmin;
import org.wso2.carbon.user.api.Tenant;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.utils.CarbonUtils;
import org.wso2.carbon.utils.ConfigurationContextService;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.*;

/**
 * This class extract server data from server admin service
 */

public class OCAgentDataExtractor {
	private static final double PERCENT = 100;
	private static final double MEGA = 1000000;
	private static final double GIGA = 1000000000;
	private static final String LOCAL_IP = "carbon.local.ip";
	private static final String MGT_TRANSPORT_HTTPS_PORT = "mgt.transport.https.port";
	private static final String NAME = "Name";
	private static final String WEB_CONTEXT_ROOT = "WebContextRoot";
	private static final String DOMAIN = "domain";
	private static final String SUB_DOMAIN = "subDomain";
	private static final String PROPERTIES = "properties";
	private static final String PROPERTY_NAME = "name";
	private static final String PROPERTY_VALUE = "value";
	private static final String PATCH_PATH =
			CarbonUtils.getCarbonHome() + "/repository/components/patches";
	private static final String PATCH = "patch";
	private static final Log logger = LogFactory.getLog(OCAgentDataExtractor.class);
	private static OCAgentDataExtractor instance =
			new OCAgentDataExtractor();
	private static OCMessage ocMessage;
	private JavaSysMon javaSysMon = new JavaSysMon();
	private String os;
	private int cpuCount;
	private double cpuSpeed;
	private double totalMemory;

	private OCAgentDataExtractor() {
		os = javaSysMon.osName();
		cpuCount = 0;
		cpuSpeed = 0;
		totalMemory = 0;
		//		cpuCount = javaSysMon.numCpus();

		//		cpuSpeed = javaSysMon.cpuFrequencyInHz() / GIGA;

		//		totalMemory = javaSysMon.physical().getTotalBytes() / MEGA;
	}

	public static OCAgentDataExtractor getInstance() {
		return instance;
	}

	public String getLocalIp() throws ParameterUnavailableException {
		String value = System.getProperty(LOCAL_IP);
		if (value == null) {
			throw new ParameterUnavailableException(LOCAL_IP + " is not available.");
		}
		return value;
	}

	public String getMgtTransportHttpsPort() throws ParameterUnavailableException {
		String value = System.getProperty(MGT_TRANSPORT_HTTPS_PORT);
		if (value == null) {
			throw new ParameterUnavailableException(
					MGT_TRANSPORT_HTTPS_PORT + " is not available.");
		}
		return value;
	}

	public String getServerName() throws ParameterUnavailableException {
		ServerConfigurationService serverConfigurationService =
				OCAgentDataHolder.getInstance().getServerConfigurationService();
		String value = serverConfigurationService.getFirstProperty(NAME);
		if (value == null) {
			throw new ParameterUnavailableException(
					MGT_TRANSPORT_HTTPS_PORT + " is not available.");
		}
		return value;
	}

	public String getServerVersion() {
		ServerAdmin serverAdmin =
				(ServerAdmin) OCAgentDataHolder.getInstance().getServerAdmin();
		if (serverAdmin != null) {
			try {
				return serverAdmin.getServerVersion();
			} catch (Exception e) {
				logger.error("Failed to retrieve server version.", e);
			}
		}
		return "Undefined";
	}

	public String getAdminServiceUrl() throws ParameterUnavailableException {
		ServerConfigurationService serverConfigurationService =
				OCAgentDataHolder.getInstance().getServerConfigurationService();
		String value = serverConfigurationService.getFirstProperty(WEB_CONTEXT_ROOT);
		if (value == null) {
			throw new ParameterUnavailableException(WEB_CONTEXT_ROOT + " is not available.");
		}
		String localIP = getLocalIp();
		String httpsPort = getMgtTransportHttpsPort();

		return "https://" + localIP + ":" + httpsPort + value;
	}

	public String getDomain() {
		String domain = "Default";
		ConfigurationContextService configurationContextService =
				OCAgentDataHolder.getInstance().getConfigurationContextService();
		if (configurationContextService != null) {
			ClusteringAgent clusteringAgent = configurationContextService.getServerConfigContext().
					getAxisConfiguration().getClusteringAgent();
			if (clusteringAgent != null) {
				Parameter domainParam = clusteringAgent.getParameter(DOMAIN);
				if (domain != null) {
					domain = domainParam.getValue().toString();
				}
			}
		}
		return domain;
	}

	public String getSubDomain() {
		String subDomain = "Default";
		ConfigurationContextService configurationContextService =
				OCAgentDataHolder.getInstance().getConfigurationContextService();
		if (configurationContextService != null) {
			ClusteringAgent clusteringAgent = configurationContextService.getServerConfigContext().
					getAxisConfiguration().getClusteringAgent();
			if (clusteringAgent != null) {
				Parameter propertiesParameter = clusteringAgent.getParameter(PROPERTIES);
				if (propertiesParameter != null) {
					OMElement omElement = propertiesParameter.getParameterElement();
					Iterator<OMElement> childElements = omElement.getChildElements();
					while (childElements.hasNext()) {
						OMElement childElement = (OMElement) childElements.next();
						if (childElement != null) {
							String propertyAttributeValue = childElement.getAttributeValue(
									childElement
											.resolveQName(
													PROPERTY_NAME));
							if (propertyAttributeValue != null &&
							    propertyAttributeValue.equalsIgnoreCase(SUB_DOMAIN)) {
								subDomain = (childElement.getAttributeValue(
										childElement.resolveQName(PROPERTY_VALUE)));
							}
						}
					}
				}
			}
		}
		return subDomain;
	}

	public String getOs() {
		return os;
	}

	public int getCpuCount() {
		return cpuCount;
	}

	public double getCpuSpeed() {
		return cpuSpeed;
	}

	public double getTotalMemory() {
		return totalMemory;
	}

	public double getFreeMemory() {
		return javaSysMon.physical().getFreeBytes() / MEGA;
	}

	public double getIdleCpuUsage() {
		long idle = javaSysMon.cpuTimes().getIdleMillis();
		double total = javaSysMon.cpuTimes().getTotalMillis();
		return (idle / total) * PERCENT;
	}

	public double getSystemCpuUsage() {
		long sys = javaSysMon.cpuTimes().getSystemMillis();
		double total = javaSysMon.cpuTimes().getTotalMillis();
		return (sys / total) * PERCENT;
	}

	public double getUserCpuUsage() {
		long user = javaSysMon.cpuTimes().getUserMillis();
		double total = javaSysMon.cpuTimes().getTotalMillis();
		return (user / total) * PERCENT;
	}

	public String getServerUpTime() {
		ServerAdmin serverAdmin =
				(ServerAdmin) OCAgentDataHolder.getInstance().getServerAdmin();
		if (serverAdmin != null) {
			try {
				ServerUpTime serverUptime = serverAdmin.getServerData().getServerUpTime();
				StringBuilder stringBuilder = new StringBuilder(64);
				stringBuilder.append(serverUptime.getDays());
				stringBuilder.append("d ");
				stringBuilder.append(serverUptime.getHours());
				stringBuilder.append("h ");
				stringBuilder.append(serverUptime.getMinutes());
				stringBuilder.append("m ");
				stringBuilder.append(serverUptime.getSeconds());
				stringBuilder.append("s");
				return stringBuilder.toString();
			} catch (Exception e) {
				logger.error("Failed to retrieve server up time.", e);
			}
		}
		return "Undefined";
	}

	public String getServerStartTime() {
		ServerAdmin serverAdmin =
				(ServerAdmin) OCAgentDataHolder.getInstance().getServerAdmin();
		if (serverAdmin != null) {
			try {
				return serverAdmin.getServerData().getServerStartTime();
			} catch (Exception e) {
				logger.error("Failed to retrieve server up time.", e);
			}
		}
		return "Undefined";
	}

	public int getThreadCount() {
		return Thread.activeCount();
	}

	public double getSystemLoadAverage() {
		OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
		return operatingSystemMXBean.getSystemLoadAverage();
	}

	public List<org.wso2.carbon.oc.agent.beans.Tenant> getTenants() {
		Tenant[] tenants = null;
		List<org.wso2.carbon.oc.agent.beans.Tenant> tenantBeanList =
				new ArrayList<org.wso2.carbon.oc.agent.beans.Tenant>();
		try {
			tenants = OCAgentDataHolder.getInstance().getRealmService().getTenantManager()
			                           .getAllTenants();
			for (Tenant t : tenants) {
				org.wso2.carbon.oc.agent.beans.Tenant tenantBean =
						new org.wso2.carbon.oc.agent.beans.Tenant();
				tenantBean.setId(t.getId());
				tenantBean.setAdminFirstName(t.getAdminFirstName());
				tenantBean.setAdminLastName(t.getAdminLastName());
				tenantBean.setAdminFullName(t.getAdminFullName());
				tenantBean.setAdminName(t.getAdminName());
				tenantBean.setCreatedDate(t.getCreatedDate());
				tenantBean.setDomain(t.getDomain());
				tenantBean.setActive(t.isActive());
				tenantBean.setEmail(t.getEmail());

				tenantBeanList.add(tenantBean);
			}
		} catch (UserStoreException e) {
			logger.error("Failed to retrieve all tenants", e);
		}
		return tenantBeanList;
	}

	public List<String> getPatches() {
		List<String> patches = new ArrayList<String>();
		File file = new File(PATCH_PATH);
		File[] patchesList = file.listFiles();
		if (patchesList != null) {
			for (File patch : patchesList) {
				if (patch.isDirectory() && patch.getName().startsWith(PATCH)) {
					patches.add(patch.getName());
				}
			}
			Collections.sort(patches);
		}
		return patches;
	}

	public Map<String, List<OCArtifact>> getArtifact() {
		List<OCArtifactProvider> artifactProviders =
				OCAgentDataHolder.getInstance().getOcArtifactProviders();
		Map<String, List<OCArtifact>> artifacts =
				new HashMap<String, List<OCArtifact>>();
		for (OCArtifactProvider ocArtifactProvider : artifactProviders) {
			artifacts.put(ocArtifactProvider.getArtifactType(), ocArtifactProvider.getArtifacts());
		}
		return artifacts;
	}

	public OCMessage getOcMessage() {
		if (ocMessage == null) {
			ocMessage = new OCMessage();
		}
		try {
			ocMessage.setLocalIp(this.getLocalIp());
			ocMessage.setServerName(this.getServerName());
			ocMessage.setServerVersion(this.getServerVersion());
			ocMessage.setDomain(this.getDomain());
			ocMessage.setSubDomain(this.getSubDomain());
			ocMessage.setServerStartTime(this.getServerStartTime());
			ocMessage.setOs(this.getOs());
			ocMessage.setTotalMemory(this.getTotalMemory());
			ocMessage.setFreeMemory(this.getFreeMemory());
			ocMessage.setCpuCount(this.getCpuCount());
			ocMessage.setCpuSpeed(this.getCpuSpeed());
			ocMessage.setAdminServiceUrl(this.getAdminServiceUrl());
			ocMessage.setThreadCount(this.getThreadCount());
			ocMessage.setSystemCpuUsage(this.getSystemCpuUsage());
			ocMessage.setIdleCpuUsage(this.getIdleCpuUsage());
			ocMessage.setUserCpuUsage(this.getUserCpuUsage());
			ocMessage.setSystemLoadAverage(this.getSystemLoadAverage());
			ocMessage.setTenants(this.getTenants());
			ocMessage.setPatches(this.getPatches());
			ocMessage.setServerUpTime(this.getServerUpTime());
			String timestamp =
					new java.text.SimpleDateFormat("MM/dd/yyyy h:mm:ss a").format(new Date());
			ocMessage.setTimestamp(timestamp);
			ocMessage.setArtifacts(this.getArtifact());
		} catch (ParameterUnavailableException e) {
			logger.error("Failed to read oc data parameters. ", e);
		}

		return ocMessage;
	}

}
