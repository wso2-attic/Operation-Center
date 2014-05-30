package org.wso2.carbon.oc.internal;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.*;

import org.wso2.carbon.oc.internal.exception.OperationsCenterMessageSendExceprion;
import org.wso2.carbon.oc.internal.messages.RegistrationRequest;
import org.wso2.carbon.oc.internal.messages.RegistrationResponse;
import org.wso2.carbon.oc.internal.messages.StatisticsUpdateMessage;
import org.wso2.carbon.server.admin.service.ServerAdmin;

/**
 * Created by jayanga on 4/18/14.
 */
public class OperationsCenterAgent implements Runnable{
    private static Logger logger = LoggerFactory.getLogger(OperationsCenterAgent.class);

    private OperationsCenterAgentComponent operationsCenterAgentComponent;
    private ServerDataExtractor serverDataExtractor;
    private OperationsCenterClient operationsCenterClient;

    private static final int numberOfExecutors = 5;
    private static final int schedulerInterval = 5;
    private static final int schedulerDelay = 0;

    private boolean isRegistered = false;
    private boolean isStopReportingStatistics = false;

    private String ocToken = null;
    private String ocaToken = null;

    private String ocURL = null;

    public OperationsCenterAgent(OperationsCenterAgentComponent operationsCenterAgentComponent) {
        this.operationsCenterAgentComponent = operationsCenterAgentComponent;
        serverDataExtractor = new ServerDataExtractor(operationsCenterAgentComponent);
        operationsCenterClient = new OperationsCenterClient();
    }

    public void start(){
        logger.info("Starting OperationsCenterAgent");

        startScheduler();
    }

    private void startScheduler(){
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(numberOfExecutors);
        ScheduledFuture scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(this, schedulerDelay, schedulerInterval, TimeUnit.SECONDS);

        logger.info("OperationsCenterAgent scheduled to run every (" + schedulerInterval + ") seconds");
    }

    @Override
    public void run() {
        if (!isRegistered){
            register();
        }
        else{
            if (isStopReportingStatistics == false){
                reportStatistics();
            }
        }
    }

    private void register(){
        ServerAdmin serverAdmin = (ServerAdmin) operationsCenterAgentComponent.getServerAdmin();
        if (serverAdmin != null){
            ocURL = serverDataExtractor.getOperationsCenterURL();
            if (ocURL != null && ocURL.length() > 0){
                try {
                    RegistrationRequest registrationRequest = createRegistrationRequest();
                    RegistrationResponse registrationResponse = sendRegistrationMessage(registrationRequest);
                    if (registrationResponse != null){
                        this.ocToken = registrationResponse.getOcToken();
                        this.ocaToken = registrationResponse.getOcaToken();
                        logger.info("Registered with Operations Center. [OCAToken=" + ocaToken + "]");

                        isRegistered = true;
                    }else{
                        logger.debug("Registration response is null.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                logger.info("Cannot connect to OC, OperationsCenterURL is not configured.");
            }
        }else{
            logger.debug("ServerAdmin is null.");
        }
    }

    private void reportStatistics(){
        StatisticsUpdateMessage statisticsUpdateMessage =  createStatisticsUpdateMessage();
        sendStatisticsUpdateMessage(statisticsUpdateMessage);
    }

    private RegistrationRequest createRegistrationRequest(){
        RegistrationRequest registrationRequest = new RegistrationRequest();
        readInitializationData(registrationRequest);

        return registrationRequest;
    }

    private StatisticsUpdateMessage createStatisticsUpdateMessage(){
        StatisticsUpdateMessage statisticsUpdateMessage = new StatisticsUpdateMessage();
        readStatisticsData(statisticsUpdateMessage);

        return statisticsUpdateMessage;
    }

    private void readStatisticsData(StatisticsUpdateMessage statisticsUpdateMessage){
        logger.info("Reading readStatisticsData data...");

        statisticsUpdateMessage.setOcToken(getOcToken());
        statisticsUpdateMessage.setStatus(serverDataExtractor.getServerStatus());
    }

    private void readInitializationData(RegistrationRequest registrationRequest){
        logger.info("Reading initialization data...");

        registrationRequest.setServerName(serverDataExtractor.getServerName());
        registrationRequest.setIp(serverDataExtractor.getServerIP());
        registrationRequest.setAdminServicePort(serverDataExtractor.getAdminServicePort());
        registrationRequest.setAdminServiceURL(serverDataExtractor.getAdminServiceURL());
        registrationRequest.setDomain(serverDataExtractor.getServerDomain());
        registrationRequest.setSubDomain(serverDataExtractor.getServerSubDomain());

        registrationRequest.setOcaToken(UUID.randomUUID().toString());
    }

    private void sendStatisticsUpdateMessage(StatisticsUpdateMessage statisticsUpdateMessage){
        ObjectMapper mapper = new ObjectMapper();

        try {
            String message = mapper.writeValueAsString(statisticsUpdateMessage);
            logger.info("Sending statistics to the Operations Center [Message=" + message + "]");

            String response = null;
            try {
                response = operationsCenterClient.sendPostMessage(ocURL + "/api/update", message, 201);
            } catch (OperationsCenterMessageSendExceprion operationsCenterMessageSendExceprion) {
                isRegistered = false;
                operationsCenterMessageSendExceprion.printStackTrace();
            }
        } catch (IOException e) {
            isRegistered = false;
            e.printStackTrace();
        }
    }

    private RegistrationResponse sendRegistrationMessage(RegistrationRequest registrationRequest){

        RegistrationResponse registrationResponse = null;

        try {

            ObjectMapper mapper = new ObjectMapper();
            try {
                String request = mapper.writeValueAsString(registrationRequest);
                logger.debug("Sending registration request to the Operations Center [Request=" + request + "]");

                String response = operationsCenterClient.sendPostMessage(ocURL + "/api/register", request, 201);
                logger.debug("Response received for the registration request from the Operations Center [Response=" + response + "]");

                registrationResponse = mapper.readValue(response, RegistrationResponse.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (OperationsCenterMessageSendExceprion operationsCenterMessageSendExceprion) {
            operationsCenterMessageSendExceprion.printStackTrace();
        }

        return registrationResponse;
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

    public ServerDataExtractor getServerDataExtractor() {
        return serverDataExtractor;
    }

    public void stopReportingStatistics() {
        this.isStopReportingStatistics = true;
    }
}
