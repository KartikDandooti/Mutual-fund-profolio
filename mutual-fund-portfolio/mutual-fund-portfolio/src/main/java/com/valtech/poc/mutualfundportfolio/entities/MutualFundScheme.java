package com.valtech.poc.mutualfundportfolio.entities;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class MutualFundScheme {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(length = 20, nullable = false)
	private String schemeName;
	@Column(length = 200, nullable = false)
	private String description;
	@Column(precision = 10, scale = 2, nullable = false)
	private BigDecimal netAssetValue;
	@ManyToOne(targetEntity = MutualFund.class, cascade = { CascadeType.MERGE, CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "type_id", referencedColumnName = "id")
	@JsonIgnore
	private MutualFund mutualFund;
	@OneToMany(targetEntity = Transaction.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "mutualFundScheme")
	private Set<Transaction> transactions;
	@OneToMany(targetEntity = UserScheme.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "scheme")
	private Set<UserScheme> userSchemes;

	public void addUserSchemes(UserScheme userScheme) {
		if (getUserSchemes() == null) {
			HashSet<UserScheme> schemeSet = new HashSet<>();
			setUserSchemes(schemeSet);
		}
		getUserSchemes().add(userScheme);
		userScheme.setScheme(this);
	}

	public void addTransactions(Transaction transaction) {
		if (getTransactions() == null) {
			HashSet<Transaction> transactionset = new HashSet<>();
			setTransactions(transactionset);
		}
		getTransactions().add(transaction);
		transaction.setMutualFundScheme(this);
	}

	public MutualFundScheme() {
	}

	public MutualFundScheme(String schemeName, String description, BigDecimal netAssetValue, MutualFund mutualFund) {
		this.schemeName = schemeName;
		this.description = description;
		this.netAssetValue = netAssetValue;
		this.mutualFund = mutualFund;
	}

	public MutualFundScheme(int id, String schemeName, String description, BigDecimal netAssetValue, MutualFund mutualFund) {
		this.id = id;
		this.schemeName = schemeName;
		this.description = description;
		this.netAssetValue = netAssetValue;
		this.mutualFund = mutualFund;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSchemeName() {
		return schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getNetAssetValue() {
		return netAssetValue;
	}

	public void setNetAssetValue(BigDecimal netAssetValue) {
		this.netAssetValue = netAssetValue;
	}

	public MutualFund getMutualFund() {
		return mutualFund;
	}

	public void setMutualFund(MutualFund mutualFund) {
		this.mutualFund = mutualFund;
	}

	public Set<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
	}

	public Set<UserScheme> getUserSchemes() {
		return userSchemes;
	}

	public void setUserSchemes(Set<UserScheme> userSchemes) {
		this.userSchemes = userSchemes;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, schemeName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MutualFundScheme other = (MutualFundScheme) obj;
		return id == other.id && Objects.equals(schemeName, other.schemeName);
	}

	@Override
	public String toString() {
		return "MutualFundScheme [id=" + id + ", schemeName=" + schemeName + ", description=" + description + ", netAssetValue=" + netAssetValue + ", mutualFund=" + mutualFund + ", transactions="
		      + transactions + ", userSchemes=" + userSchemes + "]";
	}
}