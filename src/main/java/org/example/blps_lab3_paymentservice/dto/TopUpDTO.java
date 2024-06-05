package org.example.blps_lab3_paymentservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopUpDTO {
    private String email;
    private Long billId;
    private String cardNumber;
    private int amount;

    public boolean check() {
        if (email == null || email.isEmpty() || !email.contains("@")){
            return false;
        }

        if (cardNumber == null || cardNumber.length() != 16) {
            return false;
        }

        boolean flag = false;
        for (int i = 0; i < cardNumber.length(); i++) {
            try {
                int tmp = Integer.parseInt(String.valueOf(cardNumber.charAt(i)));
            } catch (NumberFormatException numberFormatException) {
                flag = true;
            }
        }
        if (flag) return false;

        return (billId != null && amount > 0);
    }
}
