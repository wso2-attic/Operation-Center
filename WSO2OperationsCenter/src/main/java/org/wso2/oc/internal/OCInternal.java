package org.wso2.oc.internal;


import org.wso2.oc.beans.OCAgentMessage;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("internal/oc/servers/")
@Consumes("application/json")
@Produces("application/json")
public interface OCInternal {
    @POST
    public Response registerServer(OCAgentMessage ocAgentMessage) throws Exception;
    @PUT
    @Path("/{node_id}")
    public Response synchronizeServer(@PathParam("node_id") String nodeId,OCAgentMessage ocAgentMessage)
            throws Exception;

}

