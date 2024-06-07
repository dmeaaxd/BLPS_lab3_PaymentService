package org.example.blps_lab3_paymentservice.app.service;

import lombok.AllArgsConstructor;
import org.example.blps_lab3_paymentservice.app.entity.Bill;
import org.example.blps_lab3_paymentservice.app.entity.Payment;
import org.example.blps_lab3_paymentservice.app.repository.BillRepository;
import org.example.blps_lab3_paymentservice.jms.messages.FinishSubscriptionJmsMessage;
import org.example.blps_lab3_paymentservice.jms.messages.NotificationJmsMessage;
import org.example.blps_lab3_paymentservice.jms.sender.JmsNotificationSender;
import org.example.blps_lab3_paymentservice.jms.sender.JmsSubscriptionsSender;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PaymentService {
    @Autowired
    private BillRepository billRepository;

    @Autowired
    private JmsNotificationSender jmsNotificationSender;
    @Autowired
    private JmsSubscriptionsSender jmsSubscriptionsSender;

    // Пополнить
    public void topUp(Payment payment) {
        try {
            Bill bill = billRepository.findById(payment.getBillId()).orElseThrow(() -> new ObjectNotFoundException(payment.getBillId(), "Счет"));
            bill.setAccountBill(bill.getAccountBill() + payment.getAmount());
            billRepository.save(bill);

            jmsNotificationSender.sendNotification(NotificationJmsMessage.builder()
                    .to(payment.getEmail())
                    .theme("Пополнение счета")
                    .text("Ваш счет пополнен на " + payment.getAmount() + " у.е. \n" +
                            "Текущая сумма счета: " + bill.getAccountBill())
                    .build());
        } catch (ObjectNotFoundException objectNotFoundException) {
            jmsNotificationSender.sendNotification(NotificationJmsMessage.builder()
                    .to(payment.getEmail())
                    .theme("Пополнение счета")
                    .text("В процессе пополнения счета возникла ошибка, к сожалению мы не смогли найти ваш счет. \n" +
                            "Попробуйте ещё раз позже")
                    .build());
        }
    }

    // Списать
    public void writeOff(Payment payment) {
        try {
            Bill bill = billRepository.findById(payment.getBillId()).orElseThrow(() -> new ObjectNotFoundException(payment.getBillId(), "Счет"));
            if (bill.getAccountBill() < payment.getAmount()) {
                throw new Exception("На счете недостаточно средств");
            }

            bill.setAccountBill(bill.getAccountBill() - payment.getAmount());
            billRepository.save(bill);

            jmsNotificationSender.sendNotification(NotificationJmsMessage.builder()
                    .to(payment.getEmail())
                    .theme("Оформление подписки")
                    .text("С вашего счета списано " + payment.getAmount() + " у.е. для оформления подписки\n" +
                            "Текущая сумма счета: " + bill.getAccountBill())
                    .build());

            jmsSubscriptionsSender.sendFinishSubscriptionMessage(FinishSubscriptionJmsMessage.builder()
                    .email(payment.getEmail())
                    .billId(payment.getBillId())
                    .shopId(payment.getShopId())
                    .duration(payment.getDuration())
                    .amount(payment.getAmount())
                    .build());

        } catch (ObjectNotFoundException objectNotFoundException) {
            jmsNotificationSender.sendNotification(NotificationJmsMessage.builder()
                    .to(payment.getEmail())
                    .theme("Оформление подписки")
                    .text("В процессе оформления подписки произошла ошибка, к сожалению мы не смогли найти ваш счет. \n" +
                            "Попробуйте ещё раз позже")
                    .build());
        } catch (Exception e) {
            jmsNotificationSender.sendNotification(NotificationJmsMessage.builder()
                    .to(payment.getEmail())
                    .theme("Оформление подписки")
                    .text("На вашем счете недостаточно средств для оформления подписки. \n" +
                            "Пополните счет и попробуйте ещё раз")
                    .build());
        }
    }

    // Возврат средств
    public void rollbackPayment(Payment payment) {
        try {
            Bill bill = billRepository.findById(payment.getBillId()).orElseThrow(() -> new ObjectNotFoundException(payment.getBillId(), "Счет"));
            bill.setAccountBill(bill.getAccountBill() + payment.getAmount());
            billRepository.save(bill);

            jmsNotificationSender.sendNotification(NotificationJmsMessage.builder()
                    .to(payment.getEmail())
                    .theme("Возврат средств")
                    .text("Выполнен возврат средств на сумму " + payment.getAmount() + " у.е. \n" +
                            "Текущая сумма счета: " + bill.getAccountBill())
                    .build());
        } catch (ObjectNotFoundException ignored) {
        }
    }
}
