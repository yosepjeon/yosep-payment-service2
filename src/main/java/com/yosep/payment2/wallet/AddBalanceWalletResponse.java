package com.yosep.payment2.wallet;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AddBalanceWalletResponse(
	Long id,
	Long userId,
	BigDecimal balance,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {

}
