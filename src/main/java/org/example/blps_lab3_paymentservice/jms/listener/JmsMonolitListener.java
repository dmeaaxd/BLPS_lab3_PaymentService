package org.example.blps_lab3_paymentservice.jms.listener;

import jakarta.jms.TextMessage;
import lombok.AllArgsConstructor;
import org.example.blps_lab3_paymentservice.app.entity.Payment;
import org.example.blps_lab3_paymentservice.jms.listener.converter.TextMessageToObjectConverter;
import org.example.blps_lab3_paymentservice.jms.messages.TopUpJmsMessage;
import org.example.blps_lab3_paymentservice.jms.messages.WriteOffJmsMessage;
import org.example.blps_lab3_paymentservice.app.service.PaymentService;
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

    @JmsListener(destination = TOP_UP_QUEUE)
    public void receiveTopUpMessage(TextMessage textMessage) throws Exception {
        TopUpJmsMessage topUpDTO = TextMessageToObjectConverter.convert(textMessage.getText(), TopUpJmsMessage.class);
        paymentService.topUp(Payment.builder()
                .email(topUpDTO.getEmail())
                .billId(topUpDTO.getBillId())
                .amount(topUpDTO.getAmount())
                .build());
    }

    @JmsListener(destination = WRITE_OFF_QUEUE)
    public void receiveWriteOffMessage(TextMessage textMessage) throws Exception {
        WriteOffJmsMessage writeOffDTO = TextMessageToObjectConverter.convert(textMessage.getText(), WriteOffJmsMessage.class);
        paymentService.writeOff(Payment.builder()
                .email(writeOffDTO.getEmail())
                .billId(writeOffDTO.getBillId())
                .shopId(writeOffDTO.getShopId())
                .duration(writeOffDTO.getDuration())
                .amount(writeOffDTO.getAmount())
                .build());
    }
}
