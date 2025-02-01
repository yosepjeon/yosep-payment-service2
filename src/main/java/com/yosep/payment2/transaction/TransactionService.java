package com.yosep.payment2.transaction;

import com.yosep.payment2.wallet.AddBalanceWalletRequest;
import com.yosep.payment2.wallet.AddBalanceWalletResponse;
import com.yosep.payment2.wallet.FindWalletResponse;
import com.yosep.payment2.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class TransactionService {

	private final WalletService walletService;
	private final TransactionRepository transactionRepository;

	// 실제 충전에서는 userId보단 walletId가 넘어오는 케이스가 많을듯

	/**
	 * 충전요청이 여러번 발생하면? 어떻게 막을건가
	 * 1. orderId를 활용 요청이 올때 DB에 find해서 존재하면 exception -> 근본적인 해결이 될까?
	 *
	 * @param request
	 * @return
	 */

	@Transactional
	public ChargeTransactionResponse charge(ChargeTransactionRequest request) {
		// FIXME
		transactionRepository.findTransactionByOrderId(request.orderId())
			.ifPresent(transaction -> {
				throw new RuntimeException("이미 충전된 요청입니다.");
			});

		FindWalletResponse findWalletResponse = walletService.findWalletByUserId(request.userId());

		if(ObjectUtils.isEmpty(findWalletResponse)) {
			throw new RuntimeException("사용자 지갑이 존재하지 않습니다.");
		}

		AddBalanceWalletResponse addBalanceWalletResponse = walletService.addBalance(new AddBalanceWalletRequest(
			findWalletResponse.id(),
			request.amount()
		));

		Transaction transaction = Transaction.of(
			request.userId(),
			findWalletResponse.id(),
			request.orderId(),
			TransactionType.CHARGE,
			request.amount()
		);

		transactionRepository.save(transaction);
		return new ChargeTransactionResponse(addBalanceWalletResponse.id(), addBalanceWalletResponse.balance());
	}

	@Transactional
	public PaymentTransactionResponse payment(PaymentTransactionRequest request) {
		// FIXME
		transactionRepository.findTransactionByOrderId(request.courseId())
			.ifPresent(transaction -> {
				throw new RuntimeException("이미 충전된 요청입니다.");
			});

		FindWalletResponse findWalletResponse = walletService.findWalletByWalletId(request.walletId());
		AddBalanceWalletResponse addBalanceWalletResponse = walletService.addBalance(new AddBalanceWalletRequest(
			findWalletResponse.id(),
			request.amount().negate()
		));

		Transaction transaction = Transaction.createPaymentTransaction(
			findWalletResponse.userId(),
			findWalletResponse.id(),
			request.courseId(),
			request.amount()
		);

		transactionRepository.save(transaction);

		return new PaymentTransactionResponse(addBalanceWalletResponse.id(), addBalanceWalletResponse.balance());
	}
}
