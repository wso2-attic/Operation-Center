package org.wso2.carbon.oc.internal.messages;

/**
 * Created by jayanga on 4/26/14.
 */
public class StatisticsUpdateMessage extends OperationsCenterMessage {
    private String ocToken;
    private String status;

    public StatisticsUpdateMessage() {
        super(StatisticsUpdateMessage.class.getSimpleName());
    }

    public String getOcToken() {
        return ocToken;
    }

    public void setOcToken(String ocToken) {
        this.ocToken = ocToken;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
