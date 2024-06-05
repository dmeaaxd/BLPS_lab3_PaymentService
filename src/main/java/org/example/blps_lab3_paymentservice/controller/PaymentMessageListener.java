package org.example.blps_lab3_paymentservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.TextMessage;
import lombok.AllArgsConstructor;
import org.example.blps_lab3_paymentservice.dto.TopUpDTO;
import org.example.blps_lab3_paymentservice.dto.WriteOffDTO;
import org.example.blps_lab3_paymentservice.entity.Payment;
import org.example.blps_lab3_paymentservice.service.PaymentService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PaymentMessageListener {

    private final PaymentService paymentService;



    @JmsListener(destination = "topUpQueue")
    public void receiveTopUpMessage(TextMessage textMessage) throws Exception {
        TopUpDTO topUpDTO = convertTextMessageToObject(textMessage.getText(), TopUpDTO.class);

        if (topUpDTO.check()) {
            paymentService.topUp(Payment.builder()
                    .billId(topUpDTO.getBillId())
                    .amount(topUpDTO.getAmount())
                    .build());
        } else {
            throw new RuntimeException("Переданы неверные параметры в запросе");
        }
    }

    @JmsListener(destination = "writeOffQueue")
    public void receiveWriteOffMessage(TextMessage textMessage) throws Exception {
        WriteOffDTO writeOffDTO = convertTextMessageToObject(textMessage.getText(), WriteOffDTO.class);

        if (writeOffDTO.check()) {
            paymentService.writeOff(Payment.builder()
                    .billId(writeOffDTO.getBillId())
                    .amount(writeOffDTO.getAmount())
                    .build());
        } else {
            throw new RuntimeException("Переданы неверные параметры в запросе");
        }
    }


    private <T> T convertTextMessageToObject(String jsonMessage, Class<T> clazz) {
        try {
            return new ObjectMapper().readValue(jsonMessage, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    
}
