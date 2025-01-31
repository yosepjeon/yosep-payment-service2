package com.yosep.payment2;

import java.math.BigDecimal;

public record AddBalanceWalletRequest(
	Long walletId,
	BigDecimal amount
) {

}
