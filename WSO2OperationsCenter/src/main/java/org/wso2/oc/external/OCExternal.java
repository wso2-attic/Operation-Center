package org.wso2.oc.external;

import org.wso2.oc.data.Cluster;
import org.wso2.oc.data.Node;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("external/oc/clusters/")
public interface OCExternal {

	@GET
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

	@GET
	@Path("/{cluster-Id}/nodes/{node-Id}/log")
	public String requestLog(@PathParam("cluster-Id") String clusterId,
	                         @PathParam("node-Id") String nodeId);
}
