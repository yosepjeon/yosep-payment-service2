package com.yosep.payment2.transaction;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	Optional<Transaction> findTransactionByOrderId(String orderId);
}
