package org.wso2.oc.internal.impl;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.wso2.oc.data.*;
import org.wso2.oc.internal.OCInternal;

import java.util.*;

public class OCInternalService implements OCInternal {
	public Response registerServer(OCAgentMessage ocAgentMessage) {
		String serverID = registerCluster(ocAgentMessage);
		Map<String, String> response = new HashMap<String, String>();
		response.put("serverId", serverID);
		return Response.status(Response.Status.CREATED).entity(response).build();
	}

	public Response synchronizeServer(String serverId, OCAgentMessage ocAgentMessage) {
		String[] commands = updateCluster(serverId, ocAgentMessage);
		Map<String,Object> response = new HashMap<String,Object>();
		response.put("ServerCommands",commands);
		Cluster cluster = DataHolder.getClusters().get(ocAgentMessage.getDomain());
		Node node = cluster.getNodes().get(serverId);
		if(node.isLogEnabled()){
			response.put("LogEnabled",new String("true"));
			node.setLogEnabled(false);
		}else{
			response.put("LogEnabled",new String("false"));
		}
		return Response.status(Response.Status.OK).entity(response).build();
	}

	private String[] updateCluster(String nodeId, OCAgentMessage ocAgentMessage) {
		Cluster cluster = DataHolder.getClusters().get(ocAgentMessage.getDomain());
		Node node = cluster.getNodes().get(nodeId);
		String[] tempArray;
		if (!node.isRegistrationReceived()) {
			throw new WebApplicationException(
					new Throwable("Registration message has not received yet!!!"),
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
			node.setSynchronizationReceived(true);
			node.setCarbonLog(ocAgentMessage.getCarbonLog());
			cluster.setTenants(ocAgentMessage.getTenants());
			DataHolder.addNode(cluster.getClusterId(), node);
			executeClusterCommand(nodeId, cluster);
			List<String> commandNames = new ArrayList<String>();
			if (node.getCommands() != null) {
				for (Command command : node.getCommands()) {
					commandNames.add(command.getCommandName());
				}
				tempArray = commandNames.toArray(new String[commandNames.size()]);
				node.getCommands().clear();
			} else {

				tempArray = new String[0];
			}
			return tempArray;
		}

	}

	private String registerCluster(OCAgentMessage ocAgentMessage) {

		Cluster tempCluster = DataHolder.getClusters().get(ocAgentMessage.getDomain());

		if (tempCluster == null) {
			Cluster cluster = new Cluster();
			cluster.setClusterId(ocAgentMessage.getDomain());
			cluster.setClusterName(ocAgentMessage.getServerName());
			cluster.setClusterVersion(ocAgentMessage.getServerVersion());
			cluster.setDomain(ocAgentMessage.getDomain());
			cluster.setTenants(ocAgentMessage.getTenants());
			DataHolder.addCluster(cluster);
			tempCluster = cluster;
		}
		String[] temp = ocAgentMessage.getAdminServiceUrl().split("//");
		String[] tempUrl = temp[1].split("/");
		String[] serverIpPort = tempUrl[0].replaceAll("[//]", "").split(":");
		String serverId =
				serverIpPort[0].replaceAll("[.]", "") + serverIpPort[1].replaceAll("[.]", "");
		ocAgentMessage.setServerId(serverId);
		Node node = new Node();
		node.setNodeId(ocAgentMessage.getServerId());
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
		DataHolder.addNode(tempCluster.getClusterId(), node);
		return serverId;
	}

	private synchronized void executeClusterCommand(String nodeId, Cluster cluster) {
		if (!cluster.getCommands().isEmpty()) {
			cluster.updateClusterStatus();
			ClusterCommand clusterCommand = cluster.getCommands().get(0);
			if (clusterCommand.getCommandName().equals(ServerConstants.GRACEFUL_RESTART) ||
			    clusterCommand.getCommandName().equals(
					    ServerConstants.FORCE_RESTART)) {
				if (clusterCommand.getExecutedNodes().isEmpty()) {
					initializeNodeList(cluster, clusterCommand);

				} else if (clusterCommand.getNextNode().getNodeId().equals(nodeId) &&
				           (clusterCommand.isPreviousNodeUp() ||
				            clusterCommand.getPreviousNode() == null)) {

					executeCommandOnNodes(cluster, clusterCommand, nodeId);

				} else if (clusterCommand.getPreviousNode().getNodeId()
				                         .equals(nodeId)) {

					clusterCommand.setPreviousNodeUp(true);
				}
			} else if (clusterCommand.getCommandName().equals(ServerConstants.GRACEFUL_SHUTDOWN) ||
			           clusterCommand.getCommandName().equals(ServerConstants.FORCE_SHUTDOWN)) {
				if (clusterCommand.getExecutedNodes().isEmpty()) {
					initializeNodeList(cluster, clusterCommand);

				} else if (clusterCommand.getNextNode().getNodeId().equals(nodeId)) {

					executeCommandOnNodes(cluster, clusterCommand, nodeId);

				}
			}
		}
	}

	private synchronized void initializeNodeList(Cluster cluster, ClusterCommand clusterCommand) {
		Map<String, Node> nodeList = cluster.getNodes();
		for (Node temp : nodeList.values()) {
			if (temp.getStatus().equals(ServerConstants.NODE_RUNNING)) {

				clusterCommand.getExecutedNodes().put(temp.getNodeId(), false);
			}
		}
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

	private synchronized void executeCommandOnNodes(Cluster cluster, ClusterCommand clusterCommand,
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

		} else if (temp.isEmpty()) {
			clusterCommand.getExecutedNodes().clear();
			cluster.getCommands().remove(clusterCommand);
		}
	}
}