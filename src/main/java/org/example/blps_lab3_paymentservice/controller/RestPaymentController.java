package org.example.blps_lab3_paymentservice.controller;

import lombok.AllArgsConstructor;
import org.example.blps_lab3_paymentservice.dto.topUpDTO;
import org.example.blps_lab3_paymentservice.entity.Payment;
import org.example.blps_lab3_paymentservice.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class RestPaymentController {
    private PaymentService paymentService;

    @PostMapping("/topUp")
    public ResponseEntity<?> topUp(@RequestBody topUpDTO topUpDTO) {
        Map<String, String> response = new HashMap<>();

        if (!topUpDTO.check()) {
            response.put("error", "Переданы неверные параметры в запросе");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            paymentService.topUp(Payment.builder()
                    .billId(topUpDTO.getBillId())
                    .amount(topUpDTO.getAmount())
                    .build());
        } catch (Exception exception) {
            response.put("error", exception.getLocalizedMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("result", "Счет пополнен");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/writeOff")
    public ResponseEntity<?> writeOff(@RequestBody topUpDTO topUpDTO) {
        Map<String, String> response = new HashMap<>();

        if (!topUpDTO.check()) {
            response.put("error", "Переданы неверные параметры в запросе");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            paymentService.topUp(Payment.builder()
                    .billId(topUpDTO.getBillId())
                    .amount(topUpDTO.getAmount())
                    .build());
        } catch (Exception exception) {
            response.put("error", exception.getLocalizedMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("result", "Счет пополнен");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
