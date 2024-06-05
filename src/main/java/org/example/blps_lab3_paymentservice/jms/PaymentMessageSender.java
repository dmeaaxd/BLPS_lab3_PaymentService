package org.example.blps_lab3_paymentservice.jms;

import org.example.blps_lab3_paymentservice.jms.messages.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class PaymentMessageSender {
    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendNotification(Notification notification) {
        jmsTemplate.convertAndSend("notificationQueue", notification);
    }
}
