package com.yosep.payment2.wallet;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;

	private BigDecimal balance;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public Wallet(Long userId) {
		this.userId = userId;
		this.balance = BigDecimal.ZERO;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	public void addBalance(BigDecimal amount) {
		this.balance = this.balance.add(amount);
		this.updatedAt = LocalDateTime.now();
	}
}
