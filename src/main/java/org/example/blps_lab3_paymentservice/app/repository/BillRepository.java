package org.example.blps_lab3_paymentservice.app.repository;

import org.example.blps_lab3_paymentservice.app.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
}
