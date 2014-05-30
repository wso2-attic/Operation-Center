package org.wso2.carbon.oc.internal.messages;

/**
 * Created by jayanga on 4/22/14.
 */
public abstract class OperationsCenterMessage {
    private String ocMessageName;

    protected OperationsCenterMessage(String ocMessageName){
        this.ocMessageName = ocMessageName;
    }

    public String getOcMessageName() {
        return ocMessageName;
    }

    public void setOcMessageName(String ocMessageName) {
        this.ocMessageName = ocMessageName;
    }
}
