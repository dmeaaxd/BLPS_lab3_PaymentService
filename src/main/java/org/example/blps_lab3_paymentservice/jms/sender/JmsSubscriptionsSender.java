package org.example.blps_lab3_paymentservice.jms.sender;

import org.example.blps_lab3_paymentservice.jms.messages.FinishSubscriptionJmsMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class JmsSubscriptionsSender {
    @Autowired
    private JmsTemplate jmsTemplate;

    private final String FINISH_SUBSCRIPTION_QUEUE = "finishSubscriptionQueue";

    public void sendFinishSubscriptionMessage(FinishSubscriptionJmsMessage finishSubscriptionJmsMessage) {
        jmsTemplate.convertAndSend(FINISH_SUBSCRIPTION_QUEUE, finishSubscriptionJmsMessage);
    }
}
