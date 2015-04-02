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

package org.wso2.carbon.oc.agent.message;


import org.wso2.carbon.oc.agent.artifact.extractor.OCArtifactProvider;
import org.wso2.carbon.oc.agent.beans.Tenant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class allows to store all oc server info
 */
public class OCMessage {

	private String localIp;
	private String serverName;
	private String serverVersion;
	private String domain;
	private String subDomain;
	private String serverStartTime;
	private String os;
	private double totalMemory;
	private double freeMemory;
	private int cpuCount;
	private double cpuSpeed;
	private String adminServiceUrl;
	private String serverUpTime;
	private int threadCount;
	private double idleCpuUsage;
	private double systemCpuUsage;
	private double userCpuUsage;
	private double systemLoadAverage;
	private List<Tenant> tenants;
	private List<String> patches;
	private String timestamp;
    private Map<String,List<OCArtifactProvider>> artifacts;

	public String getLocalIp() {
		return localIp;
	}

	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerVersion() {
		return serverVersion;
	}

	public void setServerVersion(String serverVersion) {
		this.serverVersion = serverVersion;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getSubDomain() {
		return subDomain;
	}

	public void setSubDomain(String subDomain) {
		this.subDomain = subDomain;
	}

	public String getServerStartTime() {
		return serverStartTime;
	}

	public void setServerStartTime(String serverStartTime) {
		this.serverStartTime = serverStartTime;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public double getTotalMemory() {
		return totalMemory;
	}

	public void setTotalMemory(double totalMemory) {
		this.totalMemory = totalMemory;
	}

	public double getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(double freeMemory) {
		this.freeMemory = freeMemory;
	}

	public int getCpuCount() {
		return cpuCount;
	}

	public void setCpuCount(int cpuCount) {
		this.cpuCount = cpuCount;
	}

	public double getCpuSpeed() {
		return cpuSpeed;
	}

	public void setCpuSpeed(double cpuSpeed) {
		this.cpuSpeed = cpuSpeed;
	}

	public String getAdminServiceUrl() {
		return adminServiceUrl;
	}

	public void setAdminServiceUrl(String adminServiceUrl) {
		this.adminServiceUrl = adminServiceUrl;
	}

	public String getServerUpTime() {
		return serverUpTime;
	}

	public void setServerUpTime(String serverUpTime) {
		this.serverUpTime = serverUpTime;
	}

	public int getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	public double getIdleCpuUsage() {
		return idleCpuUsage;
	}

	public void setIdleCpuUsage(double idleCpuUsage) {
		this.idleCpuUsage = idleCpuUsage;
	}

	public double getSystemCpuUsage() {
		return systemCpuUsage;
	}

	public void setSystemCpuUsage(double systemCpuUsage) {
		this.systemCpuUsage = systemCpuUsage;
	}

	public double getSystemLoadAverage() {
		return systemLoadAverage;
	}

	public void setSystemLoadAverage(double systemLoadAverage) {
		this.systemLoadAverage = systemLoadAverage;
	}

	public List<Tenant> getTenants() {
		return tenants;
	}

	public void setTenants(List<Tenant> tenants) {
		this.tenants = tenants;
	}

	public List<String> getPatches() {
		return patches;
	}

	public void setPatches(List<String> patches) {
		this.patches = patches;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public double getUserCpuUsage() {
		return userCpuUsage;
	}

	public void setUserCpuUsage(double userCpuUsage) {
		this.userCpuUsage = userCpuUsage;
	}

	public Map<String, List<OCArtifactProvider>> getArtifacts() {
		return artifacts;
	}

	public void setArtifacts(Map<String, List<OCArtifactProvider>> artifacts) {
		this.artifacts = artifacts;
	}
}
