package org.wso2.carbon.oc.internal;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.oc.internal.exception.OperationsCenterMessageSendExceprion;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by jayanga on 4/21/14.
 */
public class OperationsCenterClient {
    private static Logger logger = LoggerFactory.getLogger(OperationsCenterClient.class);

    HttpClient httpClient = null;
    private static final String CONTENT_TYPE = "application/json";
    private static final String CHARACTER_SET = "UTF-8";

    private void connect(){
        if (httpClient == null){
            logger.debug("Crating HttpClient.");
            httpClient = new HttpClient();
        }else{
            logger.debug("HttpClient already exist.");
        }
    }

    public String sendPostMessage(String url, String message, int expectedStatusCode) throws OperationsCenterMessageSendExceprion {
        if (httpClient == null){
            connect();
        }

        String responseMessage = "";

        PostMethod postMethod = new PostMethod(url);
        //postMethod.setParameter();
        RequestEntity entity = null;
        try {
            entity = new StringRequestEntity(message, CONTENT_TYPE, CHARACTER_SET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        postMethod.setRequestEntity(entity);

        int statusCode = 0;
        try {
            statusCode = httpClient.executeMethod(postMethod);
            responseMessage = postMethod.getResponseBodyAsString();

            if (statusCode != expectedStatusCode)
            {
                throw new OperationsCenterMessageSendExceprion("Failed with HTTP error code : " + statusCode + ", ExpectedCode : " + expectedStatusCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            postMethod.releaseConnection();
        }

        return responseMessage;
    }
}
