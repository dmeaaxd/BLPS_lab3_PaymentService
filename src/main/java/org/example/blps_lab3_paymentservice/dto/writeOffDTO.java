package org.example.blps_lab3_paymentservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class writeOffDTO {
    private Long billId;
    private int amount;

    public boolean check(){
        return amount > 0;
    }
}
