package org.wso2.carbon.oc.services;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.core.ServerStatus;
import org.wso2.carbon.oc.internal.OperationsCenterAgentComponent;
import org.wso2.carbon.oc.internal.messages.CommandMessageResponse;
import org.wso2.carbon.oc.internal.messages.ControlCommandMessage;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by jayanga on 4/24/14.
 */
public class OperationsCenterAgentService {
    private static Logger logger = LoggerFactory.getLogger(OperationsCenterAgentService.class);
    private static OperationsCenterAgentComponent operationsCenterAgentComponent = null;

    public String command(OMElement omElement){
        omElement.build();
        omElement.detach();

        try {
            OMElement parentElement = AXIOMUtil.stringToOM(omElement.getText());
            String command = parentElement.getLocalName();

            if (command.equalsIgnoreCase("ControlCommand")){
                return processControlCommand(parentElement);
            }else{
                logger.info("Unknown command received. [Command=" + command + "]");
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String processControlCommand(OMElement element){

        String response = null;

        ControlCommandMessage controlCommandMessage = readControlCommandData(element);
        if (controlCommandMessage != null){
            if (validateTokens(operationsCenterAgentComponent.getOperationsCenterAgent().getOcaToken(), controlCommandMessage.getOcaToken())){
                try {
                    if(controlCommandMessage.getCommand().equalsIgnoreCase("restartGracefully")) {
                        operationsCenterAgentComponent.getServerAdmin().restartGracefully();
                        response = createCommandMessageResponse(operationsCenterAgentComponent.getOperationsCenterAgent().getOcToken(),
                                "restartGracefully", ServerStatus.STATUS_RESTARTING);
                    }else if (controlCommandMessage.getCommand().equalsIgnoreCase("restart")) {
                        operationsCenterAgentComponent.getServerAdmin().restart();
                        response = createCommandMessageResponse(operationsCenterAgentComponent.getOperationsCenterAgent().getOcToken(),
                                "restart", ServerStatus.STATUS_RESTARTING);
                    }else if (controlCommandMessage.getCommand().equalsIgnoreCase("shutdownGracefully")) {
                        operationsCenterAgentComponent.getServerAdmin().shutdownGracefully();
                        response = createCommandMessageResponse(operationsCenterAgentComponent.getOperationsCenterAgent().getOcToken(),
                                "shutdownGracefully", ServerStatus.STATUS_SHUTTING_DOWN);
                    }else if (controlCommandMessage.getCommand().equalsIgnoreCase("shutdown")) {
                        operationsCenterAgentComponent.getServerAdmin().shutdown();
                        response = createCommandMessageResponse(operationsCenterAgentComponent.getOperationsCenterAgent().getOcToken(),
                                "shutdownGracefully", ServerStatus.STATUS_SHUTTING_DOWN);
                    }else{
                        logger.info("Unknown control command. [Command=" + controlCommandMessage.getCommand() + "]");
                    }

                    operationsCenterAgentComponent.getOperationsCenterAgent().stopReportingStatistics();
                    logger.debug("Sending command response to the Operations Center [Response=" + response + "]");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                logger.info("ControlCommandMessage is not received from the valid OC.");
            }
        }else{
            logger.debug("Unable to create ControlCommandMessage.");
        }

        return response;
    }

    public static void setOperationsCenterAgentComponent(OperationsCenterAgentComponent operationsCenterAgentComponent) {
        OperationsCenterAgentService.operationsCenterAgentComponent = operationsCenterAgentComponent;
    }

    private boolean validateTokens(String ocToken, String messageOCToken){
        return ocToken.trim().equalsIgnoreCase(messageOCToken.trim());
    }

    private ControlCommandMessage readControlCommandData(OMElement omElement){
        ControlCommandMessage controlCommandMessage = new ControlCommandMessage();

        Iterator<OMElement> childElements = omElement.getChildElements();
        while (childElements.hasNext()) {
            OMElement childElement = (OMElement)childElements.next();

            if (childElement.getLocalName().equalsIgnoreCase("ocaToken")) {
                controlCommandMessage.setOcaToken(childElement.getText());
            }else if (childElement.getLocalName().equalsIgnoreCase("command")){
                controlCommandMessage.setCommand(childElement.getText());
            }else{
                logger.debug("Unhandled message field. [Field=" + childElement.getLocalName()
                        + ", Value=" + childElement.getText() + "]");
            }
        }

        return controlCommandMessage;
    }

    private String createCommandMessageResponse(String ocToken, String command, String status){
        ObjectMapper mapper = new ObjectMapper();
        CommandMessageResponse commandMessageResponse = new CommandMessageResponse();
        commandMessageResponse.setOcaToken(ocToken);
        commandMessageResponse.setCommand(command);
        commandMessageResponse.setStatus(status);

        try {
            return mapper.writeValueAsString(commandMessageResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
