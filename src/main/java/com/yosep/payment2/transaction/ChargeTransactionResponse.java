package com.yosep.payment2.transaction;

import java.math.BigDecimal;

public record ChargeTransactionResponse(
	Long walletId,
	BigDecimal balance
) {

}
