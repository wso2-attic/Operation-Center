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
	public Response synchronizeServer(@PathParam("node_id") String nodeId,
	                                  OCAgentMessage ocAgentMessage)
			throws Exception;

}

