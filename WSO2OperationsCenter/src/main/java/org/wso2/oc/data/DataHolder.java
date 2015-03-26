package org.wso2.oc.data;

import java.util.HashMap;
import java.util.Map;

public class DataHolder {
	private static Map<String,Cluster> clusters=new HashMap<String, Cluster>();

	public static Map<String, Cluster> getClusters() {
		return clusters;
	}

	public static void addCluster(Cluster cluster){
		clusters.put(cluster.getClusterId(),cluster);
	}

	public static void addNode(String clusterId,Node node){
		clusters.get(clusterId).addNewNode(node.getNodeId(),node);
	}
	private DataHolder(){
	}
}
