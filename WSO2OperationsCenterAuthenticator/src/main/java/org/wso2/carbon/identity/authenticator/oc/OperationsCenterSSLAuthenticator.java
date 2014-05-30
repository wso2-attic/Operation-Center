package org.wso2.carbon.identity.authenticator.oc;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.core.security.AuthenticatorsConfiguration;
import org.wso2.carbon.core.services.authentication.CarbonServerAuthenticator;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by jayanga on 5/29/14.
 */
public class OperationsCenterSSLAuthenticator implements CarbonServerAuthenticator {
    private static final Log log = LogFactory.getLog(OperationsCenterSSLAuthenticator.class);

    private static final int DEFAULT_PRIORITY_LEVEL = 5;
    private static final String AUTHENTICATOR_NAME = OperationsCenterSSLAuthenticator.class.getName();
    private static final String OC_ONLY_HEADER = "operations-center-only";

    @Override
    public boolean isHandle(MessageContext messageContext) {
        boolean canHandle = false;
        HttpServletRequest request = (HttpServletRequest) messageContext.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);

        if (request != null) {
            String ocOnlyHeader = request.getHeader(OC_ONLY_HEADER);

            if (ocOnlyHeader != null) {
                Object certObject = request.getAttribute("javax.servlet.request.X509Certificate");

                if (certObject != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("A valid request received with the certificate and OC_ONLY_HEADER set to '"
                                + ocOnlyHeader + "', Can handle.");
                    }
                    canHandle = true;
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("Unable to get certificate from the HttpServletRequest, Cannot handle.");
                    }
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("OC_ONLY_HEADER not set in the message, Cannot handle.");
                }
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Unable to get HttpServletRequest from the MessageContext, Cannot handle.");
            }
        }

        return canHandle;
    }

    @Override
    public boolean isAuthenticated(MessageContext messageContext) {
        boolean isAuthenticated = false;
        HttpServletRequest request = (HttpServletRequest) messageContext.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);

        if (request != null) {
            String ocOnlyHeader = request.getHeader(OC_ONLY_HEADER);

            if (ocOnlyHeader != null) {
                Object certObject = request.getAttribute("javax.servlet.request.X509Certificate");

                if (certObject != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("A valid request received with the certificate and OC_ONLY_HEADER set to '"
                                + ocOnlyHeader + "', Authenticated.");
                    }
                    isAuthenticated = true;
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("Unable to get certificate from the HttpServletRequest, Cannot authenticate.");
                    }
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("OC_ONLY_HEADER not set in the message, Cannot authenticate.");
                }
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Unable to get HttpServletRequest from the MessageContext, Cannot authenticate.");
            }
        }

        return isAuthenticated;
    }

    @Override
    public boolean authenticateWithRememberMe(MessageContext messageContext) {
        return false;
    }

    @Override
    public String getAuthenticatorName() {
        return AUTHENTICATOR_NAME;
    }

    @Override
    public int getPriority() {
        AuthenticatorsConfiguration authenticatorsConfiguration =
                AuthenticatorsConfiguration.getInstance();
        AuthenticatorsConfiguration.AuthenticatorConfig authenticatorConfig =
                authenticatorsConfiguration.getAuthenticatorConfig(AUTHENTICATOR_NAME);
        if (authenticatorConfig != null && authenticatorConfig.getPriority() > 0) {
            return authenticatorConfig.getPriority();
        }
        return DEFAULT_PRIORITY_LEVEL;
    }

    @Override
    public boolean isDisabled() {
        AuthenticatorsConfiguration authenticatorsConfiguration =
                AuthenticatorsConfiguration.getInstance();
        AuthenticatorsConfiguration.AuthenticatorConfig authenticatorConfig =
                authenticatorsConfiguration.getAuthenticatorConfig(AUTHENTICATOR_NAME);
        if (authenticatorConfig != null) {
            return authenticatorConfig.isDisabled();
        }
        return false;
    }
}
