package com.yosep.payment2;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

	Optional<Wallet> findWalletByUserId(Long userId);
}
