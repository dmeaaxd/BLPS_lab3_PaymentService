package org.example.blps_lab3_paymentservice.jms.sender;

import org.example.blps_lab3_paymentservice.jms.messages.NotificationJmsMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class JmsNotificationSender {
    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendNotification(NotificationJmsMessage notificationJmsMessage) {
        jmsTemplate.convertAndSend("notificationQueue", notificationJmsMessage);
    }
}
