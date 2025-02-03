package com.yosep.payment2;

import com.yosep.payment2.transaction.ChargeTransactionRequest;
import com.yosep.payment2.transaction.ChargeTransactionResponse;
import com.yosep.payment2.transaction.PaymentTransactionRequest;
import com.yosep.payment2.transaction.PaymentTransactionResponse;
import com.yosep.payment2.transaction.TransactionRepository;
import com.yosep.payment2.transaction.TransactionService;
import com.yosep.payment2.wallet.AddBalanceWalletRequest;
import com.yosep.payment2.wallet.CreateWalletRequest;
import com.yosep.payment2.wallet.CreateWalletResponse;
import com.yosep.payment2.wallet.WalletRepository;
import com.yosep.payment2.wallet.WalletService;
import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
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
	@Autowired
	private WalletRepository walletRepository;
	@Autowired
	private TransactionRepository transactionRepository;

	CreateWalletResponse createWalletResponse;

	@BeforeEach
	public void setUp() {
		walletRepository.deleteAll();
		transactionRepository.deleteAll();;
		createWalletResponse = walletService.createWallet(new CreateWalletRequest(1L));
		walletService.addBalance(
			new AddBalanceWalletRequest(createWalletResponse.id(), new BigDecimal(100)));
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

	@Test
	public void 충전진행() throws InterruptedException {
		// given
		String orderId = "orderId-1";
		ChargeTransactionRequest chargeTransactionRequest = new ChargeTransactionRequest(
			createWalletResponse.userId(),
			orderId,
			BigDecimal.TEN
		);

		// when
		ChargeTransactionResponse chargeTransactionResponse = transactionService.charge(
			chargeTransactionRequest);

		// then
		System.out.println(chargeTransactionResponse);
	}

	@Test
	public void 충전을_동시에_실행한다() throws InterruptedException {
		// given
		String orderId = "orderId-2";
		ChargeTransactionRequest chargeTransactionRequest = new ChargeTransactionRequest(
			createWalletResponse.userId(),
			orderId,
			BigDecimal.TEN
		);

		int numOfThread = 20;
		ExecutorService service = Executors.newFixedThreadPool(numOfThread);
		AtomicInteger completedCount = new AtomicInteger(0);

		// when
		for (int i = 0; i < numOfThread; i++) {
			service.submit(() -> {
				try {
					ChargeTransactionResponse chargeTransactionResponse = transactionService.charge(
						chargeTransactionRequest);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					completedCount.incrementAndGet();
				}
			});
		}

		// then
		service.shutdown();
		boolean finished = service.awaitTermination(1, TimeUnit.MINUTES);
		System.out.println(finished);
	}
}
