package com.yosep.payment2.wallet;

import java.math.BigDecimal;

public record CreateWalletResponse(
	Long id,
	Long userId,
	BigDecimal balance
) {

}
