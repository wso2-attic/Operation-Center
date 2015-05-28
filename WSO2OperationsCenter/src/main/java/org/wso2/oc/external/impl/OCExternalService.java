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

package org.wso2.oc.external.impl;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.oc.pubsub.controller.beans.OCCommand;
import org.wso2.carbon.oc.pubsub.controller.beans.OCMessage;
import org.wso2.carbon.oc.pubsub.controller.beans.OCMessageHolder;
import org.wso2.carbon.oc.pubsub.controller.beans.OCMessageProvider;
import org.wso2.oc.DataHolder;
import org.wso2.oc.beans.Cluster;
import org.wso2.oc.beans.Node;
import org.wso2.oc.beans.OCAgentMessage;
import org.wso2.oc.external.OCExternal;
import org.wso2.oc.internal.impl.OCInternalService;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Map;

public class OCExternalService implements OCExternal {

	private static final Log log = LogFactory.getLog(OCExternalService.class);
	private Gson gson = new Gson();

	//test method
	private void init() {
		StringBuilder sb = new StringBuilder();
		OCMessage ocMessage =
				((OCMessageHolder) PrivilegedCarbonContext.getThreadLocalCarbonContext()
				                                          .getOSGiService(OCMessageProvider.class))
						.getOCMessage();

		OCAgentMessage ocAgentMessage = new OCAgentMessage(ocMessage);

		Cluster cluster = OCInternalService.initializeCluster(ocAgentMessage);

		try {
			Node node = OCInternalService.initializeNode(ocAgentMessage);
			DataHolder.addNode(cluster.getClusterId(), node);
			OCInternalService.updateCluster(node.getNodeId(), ocAgentMessage);
		} catch (Exception e) {
			log.info(e.getMessage(), e);
		}

		// test set data
		//OCMessage ocMsg = ((OCMessageHolder)PrivilegedCarbonContext.getThreadLocalCarbonContext().getOSGiService(OCMessageProvider.class)).getOCCommand();

		//ocMsg.setCommand("RESTART");
		//ocMsg.setAdminServiceUrl(ocMessage.getAdminServiceUrl());
		ocMessage.setCommand("RESTART");

	}

	public Map<String, Cluster> getAllClustersData() {

		//		log.info("Requesting all clusters beans.");
		init();

		Map<String, Cluster> clusters = DataHolder.getClusters();

		for (Cluster cluster : clusters.values()) {
			cluster.updateClusterStatus();
		}

		return clusters;
	}

	public Cluster getClusterData(String clusterId) {

		log.debug("Requesting the beans in cluster: " + clusterId);
		init();

		Map<String, Cluster> clusters = DataHolder.getClusters();

		if (!clusters.containsKey(clusterId)) {
			throw new WebApplicationException(new Throwable("Cluster is not found"),
			                                  Response.Status.BAD_REQUEST);
		}

		Cluster cluster = clusters.get(clusterId);

		cluster.updateClusterStatus();

		return cluster;
	}

	public Map<String, Node> getAllClusterNodesData(String clusterId) {

		log.debug("Requesting all nodes beans in cluster: " + clusterId);
		init();

		Map<String, Cluster> clusters = DataHolder.getClusters();

		if (!clusters.containsKey(clusterId)) {
			throw new WebApplicationException(new Throwable("Cluster is not found"),
			                                  Response.Status.BAD_REQUEST);
		}

		Map<String, Node> nodes = clusters.get(clusterId).getNodes();

		for (Node node : nodes.values()) {
			node.updateNodeStatus();
		}

		return nodes;
	}

	public Node getClusterNodeData(String clusterId, String nodeId) {

		log.debug("Requesting the beans of the node: " + nodeId + " in the cluster: " + clusterId);
		init();

		Map<String, Cluster> clusters = DataHolder.getClusters();

		if (!clusters.containsKey(clusterId)) {
			throw new WebApplicationException(new Throwable("Cluster is not found"),
			                                  Response.Status.BAD_REQUEST);
		}

		Map<String, Node> nodes = clusters.get(clusterId).getNodes();

		if (!nodes.containsKey(nodeId)) {
			throw new WebApplicationException(new Throwable("Node is not found"),
			                                  Response.Status.BAD_REQUEST);
		}

		Node node = nodes.get(nodeId);

		node.updateNodeStatus();

		return node;
	}

	public Response executeClusterCommand(String clusterId, String commandId) {

		log.info("Executing the cluster command: " + commandId + " in the cluster: " + clusterId);

		Map<String, Cluster> clusters = DataHolder.getClusters();

		if (!clusters.containsKey(clusterId)) {
			log.error("Requested cluster is not found.");
			throw new WebApplicationException(new Throwable("Cluster is not found"),
			                                  Response.Status.BAD_REQUEST);
		}

		clusters.get(clusterId).addCommand(commandId);

		return Response.ok().build();
	}

	public Response executeNodeCommand(String clusterId, String nodeId, String commandId) {

		log.info("Executing the node command: " + commandId + " in the node: " + nodeId +
		         ", cluster: " + clusterId);

		Map<String, Cluster> clusters = DataHolder.getClusters();

		if (!clusters.containsKey(clusterId)) {
			log.error("Requested cluster is not found.");
			throw new WebApplicationException(new Throwable("Cluster is not found"),
			                                  Response.Status.BAD_REQUEST);
		}

		Map<String, Node> nodes = clusters.get(clusterId).getNodes();

		if (!nodes.containsKey(nodeId)) {
			log.error("Requested node is not found.");
			throw new WebApplicationException(new Throwable("Node is not found"),
			                                  Response.Status.BAD_REQUEST);
		}

		nodes.get(nodeId).addCommand(commandId);
		//		OCCommand ocCommand = ((OCMessageHolder)PrivilegedCarbonContext.getThreadLocalCarbonContext().getOSGiService(OCMessageProvider.class)).getOCCommand();
		//		if(ocCommand == null) {
		//			ocCommand = new OCCommand(commandId, nodes.get(nodeId).getAdminServiceUrl());
		//		}
		//		ocCommand = new OCCommand(commandId, nodes.get(nodeId).getAdminServiceUrl());
//		((OCMessageHolder) PrivilegedCarbonContext.getThreadLocalCarbonContext()
//		                                          .getOSGiService(OCMessageProvider.class))
//				.setOCCommand(
//						new OCCommand(commandId, nodes.get(nodeId).getAdminServiceUrl()));
		OCCommand ocCommand = new OCCommand("RESTART>>>", "3123");
		((OCMessageHolder) PrivilegedCarbonContext.getThreadLocalCarbonContext().getOSGiService(OCMessageProvider.class)).setOCCommand(ocCommand);
		return Response.ok().build();
	}

}