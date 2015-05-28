/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.oc.pubsub.messaging.event.node.notifier;

import org.wso2.carbon.oc.pubsub.controller.beans.OCCommand;
import org.wso2.carbon.oc.pubsub.messaging.event.Event;

/**
 * Created by noelyahan on 5/5/15.
 */
public class NodeNotifierCommandEvent extends Event {
	private OCCommand ocCommand;


	public NodeNotifierCommandEvent(OCCommand ocCommand) {
		this.ocCommand = ocCommand;
	}

	public OCCommand getOcCommand() {
		return ocCommand;
	}

	public void setOcCommand(OCCommand ocCommand) {
		this.ocCommand = ocCommand;
	}
}
