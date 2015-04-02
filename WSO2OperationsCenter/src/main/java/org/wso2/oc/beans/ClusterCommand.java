package org.wso2.oc.beans;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

public class ClusterCommand extends Command{

	@JsonIgnore
    private  Map<String,Boolean> executedNodes=new HashMap<String,Boolean>();
    @JsonIgnore
	private  Node nextNode;
    @JsonIgnore
	private  Node previousNode;
    @JsonIgnore
	private  boolean isPreviousNodeUp;

    public ClusterCommand(String commandName) {
        super(commandName);
        nextNode=null;
        previousNode=null;
        isPreviousNodeUp=false;
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
