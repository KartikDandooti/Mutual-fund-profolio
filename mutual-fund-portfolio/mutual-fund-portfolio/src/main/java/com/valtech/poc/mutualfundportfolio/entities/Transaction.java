package com.valtech.poc.mutualfundportfolio.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "userId", referencedColumnName = "id")
	@JsonIgnore
	private User user;
	@ManyToOne(targetEntity = MutualFundScheme.class, fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "schemeId", referencedColumnName = "id")
	@JsonIgnore
	private MutualFundScheme mutualFundScheme;
	@Column(precision = 10, scale = 2, nullable = false)
	private BigDecimal netAssetValue;
	@Column(precision = 10, scale = 2, nullable = false)
	private BigDecimal amount;
	@Column(precision = 10, scale = 2, nullable = false)
	private BigDecimal navUnits;
	@Column(nullable = false)
	private LocalDateTime transactionDate;
	@Column(nullable = false) 
	@Enumerated(EnumType.STRING)
	private TransactionType transactionType;

	public Transaction() {
	}

	public Transaction(User user, MutualFundScheme mutualFundScheme, BigDecimal netAssetValue, BigDecimal amount, BigDecimal navUnits, LocalDateTime date, TransactionType transactionType) {
		this.user = user;
		this.mutualFundScheme = mutualFundScheme;
		this.netAssetValue = netAssetValue;
		this.amount = amount;
		this.navUnits = navUnits;
		this.transactionDate = date;
		this.transactionType = transactionType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public MutualFundScheme getMutualFundScheme() {
		return mutualFundScheme;
	}

	public void setMutualFundScheme(MutualFundScheme mutualFundScheme) {
		this.mutualFundScheme = mutualFundScheme;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getNavUnits() {
		return navUnits;
	}

	public void setNavUnits(BigDecimal navUnits) {
		this.navUnits = navUnits;
	}

	public LocalDateTime getDate() {
		return transactionDate;
	}

	public void setDate(LocalDateTime date) {
		this.transactionDate = date;
	}

	public BigDecimal getNetAssetValue() {
		return netAssetValue;
	}

	public void setNetAssetValue(BigDecimal netAssetValue) {
		this.netAssetValue = netAssetValue;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", user=" + user + ", mutualFundScheme=" + mutualFundScheme + ", netAssetValue=" + netAssetValue + ", amount=" + amount + ", navUnits=" + navUnits + ", date="
		      + transactionDate + ", transactionType=" + transactionType + "]";
	}
}