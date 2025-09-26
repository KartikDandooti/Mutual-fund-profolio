package com.valtech.poc.mutualfundportfolio.models;

import java.math.BigDecimal;

import com.valtech.poc.mutualfundportfolio.entities.MutualFundScheme;

public class RedeemModel {

	private MutualFundScheme scheme;
	private BigDecimal nav;
	private BigDecimal schemeUnits;
	private BigDecimal amount;

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

	public BigDecimal getSchemeUnits() {
		return schemeUnits;
	}

	public void setSchemUnits(BigDecimal schemeUnits) {
		this.schemeUnits = schemeUnits;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}