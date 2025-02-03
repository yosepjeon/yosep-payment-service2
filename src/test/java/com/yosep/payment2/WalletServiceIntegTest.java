package com.yosep.payment2;

import static org.assertj.core.api.Assertions.assertThat;

import com.yosep.payment2.wallet.CreateWalletRequest;
import com.yosep.payment2.wallet.CreateWalletResponse;
import com.yosep.payment2.wallet.WalletRepository;
import com.yosep.payment2.wallet.WalletService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class WalletServiceIntegTest {

	@Autowired
	private WalletService walletService;
	@Autowired
	private WalletRepository walletRepository;

	@AfterEach
	public void tearDown() {
		walletRepository.deleteAll();
	}

	@Test
	@Transactional
	public void 지값을_생성한다() {
		// given
		CreateWalletRequest request = new CreateWalletRequest(200L);

		// when
		CreateWalletResponse response = walletService.createWallet(request);

		// then
		assertThat(response).isNotNull();
		System.out.println(response);
	}

	@Test
	public void 이미_지갑이_있고_동시에_여러건의__지갑이_생성된다면_잘될까() throws InterruptedException {
		// given
		Long userId = 2L;
		final CreateWalletRequest request = new CreateWalletRequest(userId);
		walletService.createWallet(request);

		int numOfThread = 20;
		ExecutorService service = Executors.newFixedThreadPool(numOfThread);
		AtomicInteger compltedTasks = new AtomicInteger(0);

		for (int i = 0; i < numOfThread; i++) {
			service.submit(() -> {
				try {
					walletService.createWallet(request);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					compltedTasks.incrementAndGet();
				}
			});
		}

		service.shutdown();
		boolean finished = service.awaitTermination(1, TimeUnit.MINUTES);
		System.out.println(finished);
		System.out.println(walletRepository.findAllByUserId(userId));
	}

	@Test
	public void 지갑이_없고_동시에_여러건의__지갑이_생성된다면_잘될까() throws InterruptedException {
		// given
		Long userId = 2L;
		final CreateWalletRequest request = new CreateWalletRequest(userId);

		int numOfThread = 20;
		ExecutorService service = Executors.newFixedThreadPool(numOfThread);
		AtomicInteger compltedTasks = new AtomicInteger(0);

		// when
		for (int i = 0; i < numOfThread; i++) {
			service.submit(() -> {
				try {
					walletService.createWallet(request);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					compltedTasks.incrementAndGet();
				}
			});
		}

		// then
		service.shutdown();
		boolean finished = service.awaitTermination(1, TimeUnit.MINUTES);
		System.out.println(finished);
		System.out.println(walletRepository.findAllByUserId(userId));
	}
}
