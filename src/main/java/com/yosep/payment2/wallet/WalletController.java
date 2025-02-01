package com.yosep.payment2.wallet;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WalletController {

	private final WalletService walletService;

	// TODO: 예외처리 필요
	@PostMapping("/api/wallets")
	public CreateWalletResponse createWallet(
		@RequestBody CreateWalletRequest request
	) {
		return walletService.createWallet(request);
	}

	@GetMapping("/api/users/{userId}/wallets")
	public FindWalletResponse findWalletByUserId(
		@PathVariable("userId") Long userId
	) {
		return walletService.findWalletByUserId(userId);
	}

}
