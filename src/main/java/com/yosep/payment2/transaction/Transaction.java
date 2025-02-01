package com.yosep.payment2.transaction;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long userId;
	private Long walletId;
	private String orderId;
	@Enumerated(EnumType.STRING)
	private TransactionType transactionType;
	private BigDecimal amount;
	private String description;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static Transaction of(
		Long userId,
		Long walletId,
		String orderId,
		TransactionType transactionType,
		BigDecimal amount) {
		Transaction transaction = new Transaction();
		transaction.userId = userId;
		transaction.walletId = walletId;
		transaction.orderId = orderId;
		transaction.transactionType = transactionType;
		transaction.amount = amount;
		transaction.description = "충전";
		transaction.createdAt = LocalDateTime.now();
		transaction.updatedAt = LocalDateTime.now();

		return transaction;
	}

	public static Transaction createPaymentTransaction(
		Long userId,
		Long walletId,
		String courseId,
		BigDecimal amount) {
		Transaction transaction = new Transaction();
		transaction.userId = userId;
		transaction.walletId = walletId;
		transaction.orderId = courseId;
		transaction.transactionType = TransactionType.PAYMENT;
		transaction.amount = amount;
		transaction.description = courseId + " 결제";
		transaction.createdAt = LocalDateTime.now();
		transaction.updatedAt = LocalDateTime.now();

		return transaction;
	}
}
