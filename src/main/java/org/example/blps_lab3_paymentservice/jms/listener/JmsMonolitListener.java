package org.example.blps_lab3_paymentservice.jms.listener;

import jakarta.jms.TextMessage;
import lombok.AllArgsConstructor;
import org.example.blps_lab3_paymentservice.app.entity.Payment;
import org.example.blps_lab3_paymentservice.app.service.PaymentService;
import org.example.blps_lab3_paymentservice.jms.listener.converter.TextMessageToObjectConverter;
import org.example.blps_lab3_paymentservice.jms.messages.RollbackPaymentJmsMessage;
import org.example.blps_lab3_paymentservice.jms.messages.TopUpJmsMessage;
import org.example.blps_lab3_paymentservice.jms.messages.WriteOffJmsMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class JmsMonolitListener {
    @Autowired
    private final PaymentService paymentService;

    private final String TOP_UP_QUEUE = "topUpQueue";
    private final String WRITE_OFF_QUEUE = "writeOffQueue";
    private final String ROLLBACK_PAYMENT_QUEUE = "rollbackPaymentQueue";

    @JmsListener(destination = TOP_UP_QUEUE)
    public void receiveTopUpMessage(TextMessage textMessage) throws Exception {
        TopUpJmsMessage topUpJmsMessage = TextMessageToObjectConverter.convert(textMessage.getText(), TopUpJmsMessage.class);
        paymentService.topUp(Payment.builder()
                .email(topUpJmsMessage.getEmail())
                .billId(topUpJmsMessage.getBillId())
                .amount(topUpJmsMessage.getAmount())
                .build());
    }

    @JmsListener(destination = WRITE_OFF_QUEUE)
    public void receiveWriteOffMessage(TextMessage textMessage) throws Exception {
        WriteOffJmsMessage writeOffJmsMessage = TextMessageToObjectConverter.convert(textMessage.getText(), WriteOffJmsMessage.class);

        paymentService.writeOff(Payment.builder()
                .email(writeOffJmsMessage.getEmail())
                .billId(writeOffJmsMessage.getBillId())
                .shopId(writeOffJmsMessage.getShopId())
                .duration(writeOffJmsMessage.getDuration())
                .amount(writeOffJmsMessage.getAmount())
                .build());
    }

    @JmsListener(destination = ROLLBACK_PAYMENT_QUEUE)
    public void receiveRollbackPaymentMessage(TextMessage textMessage) throws Exception {
        RollbackPaymentJmsMessage rollbackPaymentJmsMessage = TextMessageToObjectConverter.convert(textMessage.getText(), RollbackPaymentJmsMessage.class);

        paymentService.rollbackPayment(Payment.builder()
                .email(rollbackPaymentJmsMessage.getEmail())
                .billId(rollbackPaymentJmsMessage.getBillId())
                .amount(rollbackPaymentJmsMessage.getAmount())
                .build());
    }

}
