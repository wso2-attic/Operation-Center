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

package org.wso2.oc;

import org.wso2.oc.beans.Cluster;
import org.wso2.oc.beans.Node;

import java.util.HashMap;
import java.util.Map;

public class DataHolder {
	private static Map<String, Cluster> clusters = new HashMap<String, Cluster>();

	private DataHolder() {
	}

	public static Map<String, Cluster> getClusters() {
		return clusters;
	}

	public static void addCluster(Cluster cluster) {
		clusters.put(cluster.getClusterId(), cluster);
	}

	public static void addNode(String clusterId, Node node) {
		clusters.get(clusterId).addNewNode(node.getNodeId(), node);
	}
}
