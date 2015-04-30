package org.wso2.carbon.oc.agent.messaging.event.node.status;

import org.wso2.carbon.oc.agent.beans.OCMessage;
import org.wso2.carbon.oc.agent.messaging.event.Event;

/**
 * Created by noelyahan on 4/30/15.
 */
public class NodeStatusNodeActivatedEvent extends Event{
	private OCMessage ocMessage;

	public NodeStatusNodeActivatedEvent(OCMessage ocMessage) {
		this.ocMessage = ocMessage;
	}

	public OCMessage getOcMessage() {
		return ocMessage;
	}

	public void setOcMessage(OCMessage ocMessage) {
		this.ocMessage = ocMessage;
	}
}
