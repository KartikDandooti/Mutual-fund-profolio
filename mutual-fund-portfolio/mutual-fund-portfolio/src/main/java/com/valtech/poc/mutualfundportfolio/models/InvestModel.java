package com.valtech.poc.mutualfundportfolio.models;

import java.math.BigDecimal;

import com.valtech.poc.mutualfundportfolio.entities.MutualFund;
import com.valtech.poc.mutualfundportfolio.entities.MutualFundScheme;

public class InvestModel {

	private MutualFund mutualFundType;
	private MutualFundScheme scheme;
	private BigDecimal nav;
	private BigDecimal amount;
	private BigDecimal units;

	public MutualFund getMutualFundType() {
		return mutualFundType;
	}

	public void setMutualFundType(MutualFund mutualFundType) {
		this.mutualFundType = mutualFundType;
	}

	public MutualFundScheme getScheme() {
		return scheme;
	}

	public void setScheme(MutualFundScheme scheme) {
		this.scheme = scheme;
	}

	public BigDecimal getNav() {
		return nav;
	}

	public void setNav(BigDecimal nav) {
		this.nav = nav;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getUnits() {
		return units;
	}

	public void setUnits(BigDecimal units) {
		this.units = units;
	}
}