package com.yosep.payment2.wallet;

import java.math.BigDecimal;

public record AddBalanceWalletRequest(
	Long walletId,
	BigDecimal amount
) {

}
