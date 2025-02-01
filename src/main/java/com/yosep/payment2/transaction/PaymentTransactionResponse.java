package com.yosep.payment2.transaction;

import java.math.BigDecimal;

public record PaymentTransactionResponse(
	Long walletId,
	BigDecimal balance
) {

}
