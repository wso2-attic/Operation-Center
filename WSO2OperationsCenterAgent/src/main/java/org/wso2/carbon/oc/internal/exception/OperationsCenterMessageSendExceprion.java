package org.wso2.carbon.oc.internal.exception;

/**
 * Created by jayanga on 4/21/14.
 */
public class OperationsCenterMessageSendExceprion extends Exception {
    public OperationsCenterMessageSendExceprion(String message) {
        super(message);
    }

    public OperationsCenterMessageSendExceprion(String message, Exception e) {
        super(message, e);
    }
}
