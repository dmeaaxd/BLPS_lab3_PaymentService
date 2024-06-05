package org.example.blps_lab3_paymentservice.service;

import lombok.AllArgsConstructor;
import org.example.blps_lab3_paymentservice.entity.Bill;
import org.example.blps_lab3_paymentservice.entity.Payment;
import org.example.blps_lab3_paymentservice.jms.PaymentMessageSender;
import org.example.blps_lab3_paymentservice.jms.messages.Notification;
import org.example.blps_lab3_paymentservice.repository.BillRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentService {
    private PaymentMessageSender messageSender;
    private BillRepository billRepository;

    // Пополнить
    public void topUp(Payment payment) {
        try{
            Bill bill = billRepository.findById(payment.getBillId()).orElseThrow(() -> new ObjectNotFoundException(payment.getBillId(), "Счет"));
            bill.setAccountBill(bill.getAccountBill() + payment.getAmount());
            billRepository.save(bill);

            messageSender.sendNotification(Notification.builder()
                    .to(payment.getEmail())
                    .theme("Пополнение счета")
                    .text("Ваш счет пополнен на " + payment.getAmount() + " у.е. \n" +
                            "Текущая сумма счета: " + bill.getAccountBill())
                    .build());
        } catch (ObjectNotFoundException objectNotFoundException){
            messageSender.sendNotification(Notification.builder()
                    .to(payment.getEmail())
                    .theme("Пополнение счета")
                    .text("В процессе пополнения счета возникла ошибка, к сожалению мы не смогли найти ваш счет. \n" +
                            "Попробуйте ещё раз позже")
                    .build());
        }

    }

    // Списать
    public void writeOff(Payment payment){
        try {
            Bill bill = billRepository.findById(payment.getBillId()).orElseThrow(() -> new ObjectNotFoundException(payment.getBillId(), "Счет"));
            if (bill.getAccountBill() < payment.getAmount()) {
                throw new Exception("На счете недостаточно средств");
            }

            bill.setAccountBill(bill.getAccountBill() - payment.getAmount());
            billRepository.save(bill);

            messageSender.sendNotification(Notification.builder()
                    .to(payment.getEmail())
                    .theme("Оформление подписки")
                    .text("С вашего счета списано " + payment.getAmount() + " у.е. для оформления подписки\n" +
                            "Текущая сумма счета: " + bill.getAccountBill())
                    .build());

        } catch (ObjectNotFoundException objectNotFoundException){
            messageSender.sendNotification(Notification.builder()
                    .to(payment.getEmail())
                    .theme("Оформление подписки")
                    .text("В процессе оформления подписки произошла ошибка, к сожалению мы не смогли найти ваш счет. \n" +
                            "Попробуйте ещё раз позже")
                    .build());
        } catch (Exception e){
            messageSender.sendNotification(Notification.builder()
                    .to(payment.getEmail())
                    .theme("Оформление подписки")
                    .text("На вашем счете недостаточно средств для оформления подписки. \n" +
                            "Пополните счет и попробуйте ещё раз")
                    .build());
        }

    }
}
