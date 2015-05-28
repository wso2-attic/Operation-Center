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

package org.wso2.oc.external;

import org.wso2.oc.beans.Cluster;
import org.wso2.oc.beans.Node;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("external/oc/clusters/")
public interface OCExternal {

//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	public String testThis() throws Exception;

	@GET
//	@Path("/start/")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Cluster> getAllClustersData();

	@GET
	@Path("/{cluster-Id}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Cluster getClusterData(@PathParam("cluster-Id") String clusterId);

	@GET
	@Path("/{cluster-Id}/nodes/")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Node> getAllClusterNodesData(@PathParam("cluster-Id") String clusterId);

	@GET
	@Path("/{cluster-Id}/nodes/{node-Id}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Node getClusterNodeData(@PathParam("cluster-Id") String clusterId,
	                               @PathParam("node-Id") String nodeId);

	@PUT
	@Path("/{cluster-Id}/commands/{command-Id}/")
	public Response executeClusterCommand(@PathParam("cluster-Id") String clusterId,
	                                      @PathParam("command-Id") String commandId);

	@PUT
	@Path("/{cluster-Id}/nodes/{node-Id}/commands/{command-Id}/")
	public Response executeNodeCommand(@PathParam("cluster-Id") String clusterId,
	                                   @PathParam("node-Id") String nodeId,
	                                   @PathParam("command-Id") String commandId);

}
