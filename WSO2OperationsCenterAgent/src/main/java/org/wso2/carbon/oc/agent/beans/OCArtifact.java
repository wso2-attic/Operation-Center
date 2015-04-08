package org.wso2.carbon.oc.agent.beans;

import java.util.HashMap;
import java.util.Map;

public class OCArtifact {
	private long lastModifiedTime;
	private String version;
	private Map<String, Object> properties = new HashMap<String, Object>();

	public long getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(long lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
}
