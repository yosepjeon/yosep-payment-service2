package com.yosep.payment2.transaction;

import java.math.BigDecimal;

public record PaymentTransactionRequest(
	Long walletId,
	String courseId,
	BigDecimal amount
) {

}
