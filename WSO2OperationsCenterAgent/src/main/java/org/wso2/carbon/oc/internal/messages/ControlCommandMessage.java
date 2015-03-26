package org.wso2.carbon.oc.internal.messages;

/**
 * Created by jayanga on 4/29/14.
 */
public class ControlCommandMessage extends OperationsCenterMessage {
    private String ocaToken;
    private String command;

    public ControlCommandMessage() {
        super(ControlCommandMessage.class.getSimpleName());
    }

    public ControlCommandMessage(String ocaToken, String command) {
        this();
        this.ocaToken = ocaToken;
        this.command = command;
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

    @Override
    public String toString() {
        return "ControlCommandMessage{" +
                "ocaToken='" + ocaToken + '\'' +
                ", command='" + command + '\'' +
                '}';
    }
}
