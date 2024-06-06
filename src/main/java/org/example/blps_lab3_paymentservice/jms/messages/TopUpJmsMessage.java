package org.example.blps_lab3_paymentservice.jms.messages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopUpJmsMessage {
    private String email;
    private Long billId;
    private String cardNumber;
    private int amount;
}
