package com.yosep.payment2;

import com.yosep.payment2.transaction.PaymentTransactionRequest;
import com.yosep.payment2.transaction.PaymentTransactionResponse;
import com.yosep.payment2.transaction.TransactionService;
import com.yosep.payment2.wallet.AddBalanceWalletRequest;
import com.yosep.payment2.wallet.CreateWalletRequest;
import com.yosep.payment2.wallet.CreateWalletResponse;
import com.yosep.payment2.wallet.WalletService;
import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TransactionServiceIntegTest {

	@Autowired
	private TransactionService transactionService;
	@Autowired
	private WalletService walletService;

	CreateWalletResponse createWalletResponse;

	@BeforeEach
	public void setUp() {
		createWalletResponse = walletService.createWallet(new CreateWalletRequest(1L));
		walletService.addBalance(new AddBalanceWalletRequest(createWalletResponse.id(), new BigDecimal(100)));
	}

	@Test
	@Transactional
	public void 결제를_생성한다() {
		// given
		PaymentTransactionRequest request = new PaymentTransactionRequest(
			createWalletResponse.id(),
			"course-1",
			new BigDecimal(10)
		);

		// when
		PaymentTransactionResponse response = transactionService.payment(request);

		// then
		Assertions.assertNotNull(response);
		System.out.println(response);
	}
}
