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

package org.wso2.oc.internal.impl;

import org.wso2.oc.DataHolder;
import org.wso2.oc.ServerConstants;
import org.wso2.oc.beans.*;
import org.wso2.oc.internal.OCInternal;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.*;

public class OCInternalService implements OCInternal {
	/**
	 * register cluster and set node level data and add it to the cluster
	 *
	 * @param ocAgentMessage
	 * @return generated node-id
	 * @throws Exception
	 */
	public Response registerServer(OCAgentMessage ocAgentMessage) throws Exception {
		Cluster cluster = initializeCluster(ocAgentMessage);
		Node node = initializeNode(ocAgentMessage);
		DataHolder.addNode(cluster.getClusterId(), node);
		String nodeId = node.getNodeId();
		return Response.status(Response.Status.CREATED).entity(nodeId).build();
	}

	/**
	 * updates the node with the new parameters of the synchronization message
	 *
	 * @param nodeId
	 * @param ocAgentMessage
	 * @return list of commands available on the node
	 * @throws Exception
	 */
	public Response synchronizeServer(String nodeId, OCAgentMessage ocAgentMessage)
			throws Exception {
		List<String> response = updateCluster(nodeId, ocAgentMessage);
		return Response.status(Response.Status.OK).entity(response).build();
	}

	/**
	 * initialize the cluster with the data coming from registration message
	 *
	 * @param ocAgentMessage
	 * @return cluster
	 */
	public static Cluster initializeCluster(OCAgentMessage ocAgentMessage) {
		Cluster tempCluster = DataHolder.getClusters().get(ocAgentMessage.getDomain());
		if (tempCluster == null) {
			Cluster cluster =
					new Cluster(ocAgentMessage.getDomain(), ocAgentMessage.getServerName(),
					            ocAgentMessage.getServerVersion(), ocAgentMessage.getDomain(),
					            ocAgentMessage.getTenants());
			DataHolder.addCluster(cluster);
			tempCluster = cluster;
		}
		return tempCluster;
	}

	/**
	 * initialize the node with parameters of the registration method
	 *
	 * @param ocAgentMessage
	 * @return node
	 * @throws Exception
	 */
	public static Node initializeNode(OCAgentMessage ocAgentMessage) throws Exception {
		Node node = new Node();
		node.setNodeId(generateServerId(ocAgentMessage));
		node.setIp(ocAgentMessage.getIp());
		node.setSubDomain(ocAgentMessage.getSubDomain());
		node.setAdminServiceUrl(ocAgentMessage.getAdminServiceUrl());
		node.setStartTime(ocAgentMessage.getStartTime());
		node.setOs(ocAgentMessage.getOs());
		node.setTotalMemory(ocAgentMessage.getTotalMemory());
		node.setCpuCount(ocAgentMessage.getCpuCount());
		node.setCpuSpeed(ocAgentMessage.getCpuSpeed());
		node.setTimestamp(ocAgentMessage.getTimestamp());
		node.setPatches(ocAgentMessage.getPatches());
		node.setRegistrationReceived(true);
		return node;
	}

	/**
	 * generate nodeId from the parameters from registration messages
	 *
	 * @param ocAgentMessage
	 * @return
	 * @throws Exception
	 */
	public static String generateServerId(OCAgentMessage ocAgentMessage) throws Exception {
		String nodeId = "";
		if (ocAgentMessage.getAdminServiceUrl() == null || ocAgentMessage.getServerName() == null ||
		    ocAgentMessage.getServerVersion() == null) {
			throw new WebApplicationException("Could not create NodeId",
			                                  Response.Status.BAD_REQUEST);
		} else {
			String[] temp = ocAgentMessage.getAdminServiceUrl().split("//");
			String[] tempUrl = temp[1].split("/");
			String[] serverIpPort = tempUrl[0].replaceAll("[//]", "").split(":");
			nodeId =
					ocAgentMessage.getServerName().replaceAll("\\s", "") +
					ocAgentMessage.getServerVersion().replaceAll("[.]", "_") +
					ocAgentMessage.getDomain().replaceAll("[.]", "_") + serverIpPort[0].replaceAll(
							"[.]", "_") +
					serverIpPort[1].replaceAll("[.]", "_");
		}
		nodeId = nodeId.replaceAll("\\s+","");
		return nodeId;
	}

	/**
	 * update the details of the cluster with synchronization message
	 *
	 * @param nodeId
	 * @param ocAgentMessage
	 * @return
	 * @throws Exception
	 */
	public static List<String> updateCluster(String nodeId, OCAgentMessage ocAgentMessage)
			throws Exception {
		Cluster cluster = DataHolder.getClusters().get(ocAgentMessage.getDomain());
		Node node = cluster.getNodes().get(nodeId);
		Node temp = updateNode(node, ocAgentMessage);
		cluster.setTenants(ocAgentMessage.getTenants());
		DataHolder.addNode(cluster.getClusterId(), temp);
		setClusterCommand(nodeId, cluster);
		List<String> commandNames = new ArrayList<String>();
		if (temp.getCommands() != null) {
			for (Command command : temp.getCommands()) {
				commandNames.add(command.getCommandName());
			}

			temp.getCommands().clear();
		}
		return commandNames;
	}

	/**
	 * update the node related info with the synchronization message
	 *
	 * @param node
	 * @param ocAgentMessage
	 * @return updated node
	 * @throws Exception if the server sends registration message without sending synchronization
	 *                   message
	 */
	private static Node updateNode(Node node, OCAgentMessage ocAgentMessage) throws Exception {
		if (!node.isRegistrationReceived()) {
			throw new WebApplicationException("Registration Message has not received yet",
			                                  Response.Status.BAD_REQUEST);
		} else {
			node.setFreeMemory(ocAgentMessage.getFreeMemory());
			node.setIdleCpuUsage(ocAgentMessage.getIdleCpuUsage());
			node.setSystemCpuUsage(ocAgentMessage.getSystemCpuUsage());
			node.setUserCpuUsage(ocAgentMessage.getUserCpuUsage());
			node.setServerUpTime(ocAgentMessage.getServerUpTime());
			node.setThreadCount(ocAgentMessage.getThreadCount());
			node.setSystemLoadAverage(ocAgentMessage.getSystemLoadAverage());
			node.setTimestamp(ocAgentMessage.getTimestamp());
			node.setArtifacts(
					ocAgentMessage.getArtifacts());
			node.setSynchronizationReceived(true);
		}
		return node;
	}

	/**
	 * checks if there are any cluster commands available and executes those in
	 * round robin fashion
	 *
	 * @param nodeId
	 * @param cluster
	 */
	private static synchronized void setClusterCommand(String nodeId, Cluster cluster) {
		if (!cluster.getCommands().isEmpty()) {
			//if there is any command available for the cluster update all nodes in cluster
			cluster.updateClusterStatus();
			ClusterCommand clusterCommand = cluster.getCommands().get(0);
			if (clusterCommand.getCommandName().equals(ServerConstants.GRACEFUL_RESTART) ||
			    clusterCommand.getCommandName().equals(
					    ServerConstants.FORCE_RESTART)) {
				//checks if we have initialized nodes for executing cluster wide operations
				if (clusterCommand.getExecutedNodes().isEmpty()) {
					initializeNodeList(cluster, clusterCommand);
				/*checks if the next node  to be executed the command is same as the current
				node which is sending synchronization request
				*/
				} else if (clusterCommand.getNextNode().getNodeId().equals(nodeId) &&
				           (clusterCommand.isPreviousNodeUp() ||
				            clusterCommand.getPreviousNode() == null)) {

					setCommandOnNodes(cluster, clusterCommand, nodeId);
					//checks whether the previously executed node is up again
				} else if (clusterCommand.getPreviousNode().getNodeId()
				                         .equals(nodeId)) {

					clusterCommand.setPreviousNodeUp(true);
				}
				//execute SHUTDOWN command on the cluster
			} else if (clusterCommand.getCommandName().equals(ServerConstants.GRACEFUL_SHUTDOWN) ||
			           clusterCommand.getCommandName().equals(ServerConstants.FORCE_SHUTDOWN)) {
				if (clusterCommand.getExecutedNodes().isEmpty()) {
					initializeNodeList(cluster, clusterCommand);

				} else if (clusterCommand.getNextNode().getNodeId().equals(nodeId)) {

					setCommandOnNodes(cluster, clusterCommand, nodeId);

				}
			}
		}
	}

	/**
	 * initialize the node list to be executed for cluster wide operations
	 *
	 * @param cluster
	 * @param clusterCommand
	 */
	private static synchronized void initializeNodeList(Cluster cluster, ClusterCommand clusterCommand) {
		Map<String, Node> nodeList = cluster.getNodes();
		for (Node temp : nodeList.values()) {
			if (temp.getStatus().equals(ServerConstants.NODE_RUNNING)) {
				clusterCommand.getExecutedNodes().put(temp.getNodeId(), false);
			}
		}
		//set previous node and next node parameters in the node list if the nodes are active
		if (cluster.getNumberOfActiveNodes() > 0) {
			Iterator<Map.Entry<String, Boolean>> iterator =
					clusterCommand.getExecutedNodes().entrySet().iterator();
			String nextNodeId = iterator.next().getKey();
			clusterCommand.setNextNode(cluster.getNodes().get(nextNodeId));
			clusterCommand.setPreviousNode(null);

		} else {
			cluster.getCommands().clear();
		}
	}

	/**
	 * sets the commands for nodes in round-robin
	 * fashion and updates the node list and set the next node to execute command
	 *
	 * @param cluster
	 * @param clusterCommand
	 * @param nodeId
	 */
	private static synchronized void setCommandOnNodes(Cluster cluster, ClusterCommand clusterCommand,
	                                            String nodeId) {
		Iterator<Map.Entry<String, Boolean>> iterator;
		Node currentNode = cluster.getNodes().get(nodeId);
		currentNode.getCommands().clear();
		currentNode.addCommand(clusterCommand.getCommandName());
		clusterCommand.getExecutedNodes().put(nodeId, true);
		Map<String, Boolean> temp = new HashMap<String, Boolean>();
		iterator = clusterCommand.getExecutedNodes().entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Boolean> tempEntry = iterator.next();
			//gets only the remaining nodes to execute cluster command
			if (!tempEntry.getValue()) {
				temp.put(tempEntry.getKey(), tempEntry.getValue());
			}
		}
		if (!temp.isEmpty()) {
			iterator = temp.entrySet().iterator();
			String nextNodeId = iterator.next().getKey();
			if (clusterCommand.getCommandName().equals(ServerConstants.FORCE_RESTART) ||
			    clusterCommand.getCommandName().equals(
					    ServerConstants.GRACEFUL_RESTART)) {
				clusterCommand.setPreviousNode(clusterCommand.getNextNode());
				clusterCommand.setPreviousNodeUp(false);
			}
			clusterCommand.setNextNode(cluster.getNodes().get(nextNodeId));
		 /*if the command is executed on all the nodes clear the command list and remove the
		   executed command from cluster command list
		   */
		} else if (temp.isEmpty()) {
			clusterCommand.getExecutedNodes().clear();
			cluster.getCommands().remove(clusterCommand);
		}
	}
}