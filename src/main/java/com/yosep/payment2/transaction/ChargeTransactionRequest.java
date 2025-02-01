package com.yosep.payment2.transaction;

import java.math.BigDecimal;

public record ChargeTransactionRequest(
	Long userId,
	String orderId,
	BigDecimal amount
) {

}
