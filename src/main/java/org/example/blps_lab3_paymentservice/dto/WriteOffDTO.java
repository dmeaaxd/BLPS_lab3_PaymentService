package org.example.blps_lab3_paymentservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WriteOffDTO {
    private String email;
    private Long billId;
    private int amount;

    public boolean check() {
        return (billId != null && amount > 0) && !(email == null || email.isEmpty() || !email.contains("@"));
    }
}
