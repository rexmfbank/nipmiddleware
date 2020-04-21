package com.globalaccelerex.nipmiddleware.messaging;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.globalaccelerex.nipmiddleware.config.AppConfig;
import com.globalaccelerex.nipmiddleware.logging.api.IMarker;
import com.globalaccelerex.nipmiddleware.logging.impl.Marker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.core.SqsMessageHeaders;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@Slf4j
public class SQSService {

    @Autowired
    QueueMessagingTemplate messagingTemplate;

    public final static int DEFAULT_MAX_WAIT_IN_SECONDS = 60;

    @Autowired
    AppConfig config;

    @Autowired
    QueueTransactionHandler handler;

    public String send(Object message, Integer delaySecond) {
        messagingTemplate.convertAndSend(config.getEvent().getQueue().getName(), message, Collections.singletonMap(SqsMessageHeaders.SQS_DELAY_HEADER, delaySecond));
        return "OK";
    }

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(
            AmazonSQSAsync amazonSQSAsync) {
        return new QueueMessagingTemplate(amazonSQSAsync);
    }

    @SqsListener("${app.event.queue.name:N/A}")
    public void receiveMessage(QueuePayload message,  @Headers Map<String, String> headers) {
        IMarker marker = Marker.fromString();
        marker.info("Handler is receiving Message " + message.toString());
        marker.setMainRequest("", message.toString(), false);
        try {
            // process queue message ;
            message = handler.handlePayload(marker,message);
            if (message.isReQueue()) {
                resendMessage(message);
            }
        } catch (Exception ex) {
            // requeue message
            marker.info("Error occurred while processing queue message", ex);
            resendMessage(message);
        } finally {
            marker.setMainResponse(message.toString(), false);
            marker.done();
        }

    }

    private void resendMessage(QueuePayload message){
        send(message, (message.getWaitDuration() <=0 || message.getWaitDuration() >= DEFAULT_MAX_WAIT_IN_SECONDS  ) ? DEFAULT_MAX_WAIT_IN_SECONDS :message.getWaitDuration() );
    }
}
