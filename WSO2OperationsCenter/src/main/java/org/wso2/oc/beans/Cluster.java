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

package org.wso2.oc.beans;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.oc.ServerConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class holds all information about the cluster
 */
public class Cluster {

	private Map<String, Node> nodes;
	private String clusterId;
	private String clusterName;
	private String clusterVersion;
	private String domain;
	private List<Tenant> tenants;
	private String status;
	private List<ClusterCommand> commands;
	private static final Log log = LogFactory.getLog(Cluster.class);

	public Cluster(String clusterId, String clusterName, String clusterVersion, String domain,
	               List<Tenant> tenants) {
		nodes = new HashMap<String, Node>();
		commands = new ArrayList<ClusterCommand>();
		this.clusterId = clusterId;
		this.clusterName = clusterName;
		this.clusterVersion = clusterVersion;
		this.domain = domain;
		this.tenants = tenants;
	}

	public int getNumberOfNodes() {
		return nodes.size();
	}

	public int getNumberOfActiveNodes() {
		int numberOfActiveNodes = 0;
		for (Node n : nodes.values()) {
			if (n.getStatus() == ServerConstants.NODE_RUNNING) {
				numberOfActiveNodes++;
			}
		}
		return numberOfActiveNodes;
	}

	/**
	 * update the status of all nodes in the cluster
	 */
	public void updateClusterStatus() {
		boolean allNodesDown = true;
		for (Node node : nodes.values()) {
			node.updateNodeStatus();
			if (node.getStatus().equals(ServerConstants.NODE_RUNNING)) {
				allNodesDown = false;
			}
		}
		if (allNodesDown) {
			this.setStatus(ServerConstants.CLUSTER_DOWN);
		} else {
			this.setStatus(ServerConstants.CLUSTER_RUNNING);
		}
	}

	public Map<String, Node> getNodes() {
		return this.nodes;
	}

	public void addNewNode(String nodeId, Node node) {
		this.nodes.put(nodeId, node);
	}

	public String getClusterId() {
		return clusterId;
	}

	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public String getClusterVersion() {
		return clusterVersion;
	}

	public void setClusterVersion(String clusterVersion) {
		this.clusterVersion = clusterVersion;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public List<Tenant> getTenants() {
		return tenants;
	}

	public void setTenants(List<Tenant> tenants) {
		this.tenants = tenants;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<ClusterCommand> getCommands() {
		return commands;
	}

	public void addCommand(String commandId) {
		if (commands == null) {
			commands = new ArrayList<ClusterCommand>();
		}
		commands.add(new ClusterCommand(commandId));
	}
}
