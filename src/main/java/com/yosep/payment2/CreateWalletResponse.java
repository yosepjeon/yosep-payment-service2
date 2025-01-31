package com.yosep.payment2;

import java.math.BigDecimal;

public record CreateWalletResponse(
	Long id,
	Long userId,
	BigDecimal balance
) {

}
