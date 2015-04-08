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

package org.wso2.oc.beans;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

/**
 * keeps information about the nodeList that needs to be executed cluster command
 */
public class ClusterCommand extends Command {

	@JsonIgnore
	private Map<String, Boolean> executedNodes = new HashMap<String, Boolean>();
	@JsonIgnore
	private Node nextNode;
	@JsonIgnore
	private Node previousNode;
	@JsonIgnore
	private boolean isPreviousNodeUp;

	public ClusterCommand(String commandName) {
		super(commandName);
		nextNode = null;
		previousNode = null;
		isPreviousNodeUp = false;
	}

	public Map<String, Boolean> getExecutedNodes() {
		return executedNodes;
	}

	public void setExecutedNodes(Map<String, Boolean> executedNodes) {
		this.executedNodes = executedNodes;
	}

	public Node getNextNode() {
		return nextNode;
	}

	public void setNextNode(Node nextNode) {
		this.nextNode = nextNode;
	}

	public Node getPreviousNode() {
		return previousNode;
	}

	public void setPreviousNode(Node previousNode) {
		this.previousNode = previousNode;
	}

	public boolean isPreviousNodeUp() {
		return isPreviousNodeUp;
	}

	public void setPreviousNodeUp(boolean isPreviousNodeUp) {
		this.isPreviousNodeUp = isPreviousNodeUp;
	}

}
