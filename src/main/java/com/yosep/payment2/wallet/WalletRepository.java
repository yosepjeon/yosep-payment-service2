package com.yosep.payment2.wallet;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

	Optional<Wallet> findTopByUserId(Long userId);

	List<Wallet> findAllByUserId(Long userId);
}
