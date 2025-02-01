package com.yosep.payment2

import com.yosep.payment2.transaction.ChargeTransactionRequest
import com.yosep.payment2.transaction.PaymentTransactionRequest
import com.yosep.payment2.transaction.Transaction
import com.yosep.payment2.transaction.TransactionRepository
import com.yosep.payment2.transaction.TransactionService
import com.yosep.payment2.transaction.TransactionType
import com.yosep.payment2.wallet.AddBalanceWalletResponse
import com.yosep.payment2.wallet.FindWalletResponse
import com.yosep.payment2.wallet.WalletService
import spock.lang.Specification

import java.time.LocalDateTime


class TransactionServiceSpockTest extends Specification {
    TransactionService transactionService
    WalletService walletService = Mock()
    TransactionRepository transactionRepository = Mock()

    def setup() {
        transactionService = new TransactionService(walletService, transactionRepository)
    }

    def "충전 트랜잭션이 성공한다."() {
        given:
        ChargeTransactionRequest request = new ChargeTransactionRequest(1L, "orderId", BigDecimal.TEN)
        transactionRepository.findTransactionByOrderId(request.orderId()) >> Optional.empty()

        def findWalletResponse = new FindWalletResponse(
                1L,
                1L,
                BigDecimal.ZERO,
                LocalDateTime.now(),
                LocalDateTime.now())
        walletService.findWalletByUserId(1L) >> findWalletResponse
        def addBalanceWalletResponse = new AddBalanceWalletResponse(
                1L,
                1L,
                findWalletResponse.balance().add(request.amount()),
                LocalDateTime.now(),
                LocalDateTime.now())
        walletService.addBalance(_) >> addBalanceWalletResponse

        when:
        def createdWallet = transactionService.charge(request)

        then:
        1 * transactionRepository.save(_)
        createdWallet != null
        println(createdWallet)
    }

    def "지갑이 없다면 충전이 실패한다."() {
        given:
        ChargeTransactionRequest request = new ChargeTransactionRequest(1L, "orderId", BigDecimal.TEN)
        transactionRepository.findTransactionByOrderId(request.orderId()) >> Optional.empty()

        def findWalletResponse = new FindWalletResponse(
                1L,
                1L,
                BigDecimal.ZERO,
                LocalDateTime.now(),
                LocalDateTime.now())
        walletService.findWalletByUserId(1L) >> null

        when:
        def createdWallet = transactionService.charge(request)

        then:
        def ex = thrown(RuntimeException)
        ex != null
        ex.printStackTrace()
    }

    def "이미 충전됐다면 충전 트랜잭션이 실패한다."() {
        given:
        ChargeTransactionRequest request = new ChargeTransactionRequest(1L, "orderId", BigDecimal.TEN)
        transactionRepository.findTransactionByOrderId(request.orderId()) >> Optional.of(Transaction.of(1L, 1L, "orderId", TransactionType.CHARGE,BigDecimal.TEN))

        when:
        def createdWallet = transactionService.charge(request)

        then:
        def ex = thrown(RuntimeException)
        ex != null
        ex.printStackTrace()
    }

    def "결제 트랜잭션이 성공한다."() {
        given:
        PaymentTransactionRequest request = new PaymentTransactionRequest(1L, "100", BigDecimal.TEN)
        transactionRepository.findTransactionByOrderId(request.courseId()) >> Optional.empty()

        def findWalletResponse = new FindWalletResponse(
                1L,
                1L,
                BigDecimal.TEN,
                LocalDateTime.now(),
                LocalDateTime.now())
        walletService.findWalletByWalletId(1L) >> findWalletResponse
        def addBalanceWalletResponse = new AddBalanceWalletResponse(
                1L,
                1L,
                findWalletResponse.balance().add(request.amount().negate()),
                LocalDateTime.now(),
                LocalDateTime.now())
        walletService.addBalance(_) >> addBalanceWalletResponse

        when:
        def createdWallet = transactionService.payment(request)

        then:
        1 * transactionRepository.save(_)
        createdWallet != null
        println(createdWallet)
    }
}
