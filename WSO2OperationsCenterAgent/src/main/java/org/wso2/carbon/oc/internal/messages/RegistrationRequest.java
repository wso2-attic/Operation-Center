package org.wso2.carbon.oc.internal.messages;

/**
 * Created by jayanga on 4/18/14.
 */
public class RegistrationRequest extends OperationsCenterMessage {
    private String ip;
    private String adminServicePort;
    private String adminServiceURL;
    private String ocaToken;
    private String type;
    private String serverName;
    private String domain;
    private String subDomain;
    private String mode;

    public RegistrationRequest(){
        super(RegistrationRequest.class.getSimpleName());
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAdminServicePort() {
        return adminServicePort;
    }

    public void setAdminServicePort(String adminServicePort) {
        this.adminServicePort = adminServicePort;
    }

    public String getAdminServiceURL() {
        return adminServiceURL;
    }

    public void setAdminServiceURL(String adminServiceURL) {
        this.adminServiceURL = adminServiceURL;
    }

    public String getOcaToken() {
        return ocaToken;
    }

    public void setOcaToken(String ocaToken) {
        this.ocaToken = ocaToken;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSubDomain() {
        return subDomain;
    }

    public void setSubDomain(String subDomain) {
        this.subDomain = subDomain;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return "RegistrationRequest{" +
                "ip='" + ip + '\'' +
                ", adminServicePort='" + adminServicePort + '\'' +
                ", adminServiceURL='" + adminServiceURL + '\'' +
                ", ocaToken='" + ocaToken + '\'' +
                ", type='" + type + '\'' +
                ", serverName='" + serverName + '\'' +
                ", domain='" + domain + '\'' +
                ", subDomain='" + subDomain + '\'' +
                ", mode='" + mode + '\'' +
                '}';
    }
}
