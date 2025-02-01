package com.yosep.payment2;

import static org.assertj.core.api.Assertions.assertThat;

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
}
