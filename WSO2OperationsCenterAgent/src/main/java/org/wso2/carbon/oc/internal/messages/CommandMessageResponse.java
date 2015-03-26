package org.wso2.carbon.oc.internal.messages;

/**
 * Created by jayanga on 4/25/14.
 */
public class CommandMessageResponse extends OperationsCenterMessage {
    private String ocaToken;
    private String command;
    private String status;

    public CommandMessageResponse() {
        super(CommandMessageResponse.class.getSimpleName());
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOcaToken() {
        return ocaToken;
    }

    public void setOcaToken(String ocaToken) {
        this.ocaToken = ocaToken;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
