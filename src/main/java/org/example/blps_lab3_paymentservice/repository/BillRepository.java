package org.example.blps_lab3_paymentservice.repository;

import org.example.blps_lab3_paymentservice.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {
}
