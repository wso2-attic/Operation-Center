package org.wso2.carbon.oc.internal.messages;

/**
 * Created by jayanga on 4/22/14.
 */
public class RegistrationResponse extends OperationsCenterMessage {
    private String ocToken;
    private String ocaToken;

    public RegistrationResponse(){
        super(RegistrationResponse.class.getSimpleName());
    }

    public String getOcToken() {
        return ocToken;
    }

    public void setOcToken(String ocToken) {
        this.ocToken = ocToken;
    }

    public String getOcaToken() {
        return ocaToken;
    }

    public void setOcaToken(String ocaToken) {
        this.ocaToken = ocaToken;
    }

    @Override
    public String toString() {
        return "RegistrationResponse{" +
                "ocToken='" + ocToken + '\'' +
                ", ocaToken='" + ocaToken + '\'' +
                '}';
    }
}
