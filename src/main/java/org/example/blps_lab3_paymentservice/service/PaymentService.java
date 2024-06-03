package org.example.blps_lab3_paymentservice.service;

import org.example.blps_lab3_paymentservice.entity.Bill;
import org.example.blps_lab3_paymentservice.entity.Payment;
import org.example.blps_lab3_paymentservice.repository.BillRepository;
import org.hibernate.ObjectNotFoundException;

import java.util.Optional;

public class PaymentService {
    private BillRepository billRepository;

    // Пополнить
    public void topUp(Payment payment) {
        Bill bill = billRepository.findById(payment.getBillId()).orElseThrow(() -> new ObjectNotFoundException(payment.getBillId(), "Счет"));
        bill.setAccountBill(bill.getAccountBill() + payment.getAmount());
        billRepository.save(bill);
    }

    // Списать
    public void writeOff(Payment payment) throws Exception {
        Bill bill = billRepository.findById(payment.getBillId()).orElseThrow(() -> new ObjectNotFoundException(payment.getBillId(), "Счет"));
        if (bill.getAccountBill() < payment.getAmount()){
            throw new Exception("На счете недостаточно средств");
        }
        else{
            bill.setAccountBill(bill.getAccountBill() - payment.getAmount());
            billRepository.save(bill);
        }
    }
}
