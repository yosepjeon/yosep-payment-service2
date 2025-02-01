package com.yosep.payment2

import com.yosep.payment2.wallet.AddBalanceWalletRequest
import com.yosep.payment2.wallet.CreateWalletRequest
import com.yosep.payment2.wallet.Wallet
import com.yosep.payment2.wallet.WalletRepository
import com.yosep.payment2.wallet.WalletService
import spock.lang.Specification;

class WalletSpockTest extends Specification {

    WalletService walletService;
    WalletRepository walletRepository = Mock()

    def setup() {
        walletService = new WalletService(walletRepository);
    }

    def "지갑 생성 요청 시 지갑을 갖고있지 않다면 생성된다."() {
        given:
        CreateWalletRequest request = new CreateWalletRequest(1L)
        walletRepository.findWalletByUserId(1L) >> Optional.empty()
        walletRepository.save(_) >> new Wallet(request.userId)

        when:
        def createdWallet = walletService.createWallet(request)

        then:
        1 * walletRepository.save(_) >> new Wallet(1L)
        createdWallet != null
        createdWallet.balance() == BigDecimal.ZERO
        println(createdWallet)
    }

    def "지갑 생성 요청 시 지갑을 이미 갖고 있다면 오류를 응답한다."() {
        given:
        CreateWalletRequest request = new CreateWalletRequest(1L)
        walletRepository.findWalletByUserId(1L) >> Optional.of(new Wallet(1L))
        walletRepository.save(_) >> new Wallet(request.userId)

        when:
        def createdWallet = walletService.createWallet(request)

        then:
        def ex = thrown(RuntimeException)
        ex != null
        ex.printStackTrace()
    }

    def "지갑을 조회한다. - 생성되어 있는 경우"() {
        given:
        def userId = 1L
        def wallet = new Wallet(userId)
        wallet.balance = new BigDecimal(1000)
        walletRepository.findWalletByUserId(userId) >> Optional.of(wallet)

        when:
        def result = walletService.findWalletByUserId(userId)

        then:
        result != null
        result.balance() == new BigDecimal(1000)
        println(result)
    }

    def "지갑을 조회한다. - 생성되어 있지 않은 경우"() {
        given:
        def userId = 1L
        def wallet = new Wallet(userId)
        wallet.balance = new BigDecimal(1000)
        walletRepository.findWalletByUserId(userId) >> Optional.empty()

        when:
        def result = walletService.findWalletByUserId(userId)

        then:
        result == null
        println(result)
    }

    def "지갑에 금액을 충전한다. - 정상 케이스"() {
        given:
        def userId = 1L
        def wallet = new Wallet(userId)
        def walletId = 1L
        wallet.balance = new BigDecimal(1000)
        walletRepository.findById(walletId) >> Optional.of(wallet)
        walletRepository.save(_) >> wallet
        def addBalanceRequest = new AddBalanceWalletRequest(walletId, new BigDecimal(1000))

        when:
        def result = walletService.addBalance(addBalanceRequest)

        then:
        result != null
        result.balance() == new BigDecimal(2000)
        println(result)
    }

    def "지갑에 금액을 충전한다. - 지갑이 존재하지 않는 경우"() {
        given:
        def userId = 1L
        def wallet = new Wallet(userId)
        def walletId = 1L
        wallet.balance = new BigDecimal(1000)
        walletRepository.findWalletByUserId(walletId) >> Optional.empty()
        def addBalanceRequest = new AddBalanceWalletRequest(walletId, new BigDecimal(1000))

        when:
        def result = walletService.addBalance(addBalanceRequest)

        then:
        def ex = thrown(RuntimeException)
        ex != null
        ex.printStackTrace()
    }

    def "지갑에 금액을 충전한다. - 잔액이 충분하지 않는 경우"() {
        given:
        def userId = 1L
        def wallet = new Wallet(userId)
        def walletId = 1L
        wallet.balance = new BigDecimal(1000)
        walletRepository.findById(walletId) >> Optional.of(wallet)
        walletRepository.save(_) >> wallet
        def addBalanceRequest = new AddBalanceWalletRequest(walletId, new BigDecimal(-1100))

        when:
        def result = walletService.addBalance(addBalanceRequest)

        then:
        def ex = thrown(RuntimeException)
        ex != null
        ex.printStackTrace()
    }

    def "지갑에 금액을 충전한다. - 최대 충전 한도를 초과한 경우"() {
        given:
        def userId = 1L
        def wallet = new Wallet(userId)
        def walletId = 1L
        wallet.balance = new BigDecimal(1000)
        walletRepository.findById(walletId) >> Optional.of(wallet)
        walletRepository.save(_) >> wallet
        def addBalanceRequest = new AddBalanceWalletRequest(walletId, new BigDecimal(100_001))

        when:
        def result = walletService.addBalance(addBalanceRequest)

        then:
        def ex = thrown(RuntimeException)
        ex != null
        ex.printStackTrace()
    }
}
