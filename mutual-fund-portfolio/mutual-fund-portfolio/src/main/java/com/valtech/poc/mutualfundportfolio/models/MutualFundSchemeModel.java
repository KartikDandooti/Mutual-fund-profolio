package com.valtech.poc.mutualfundportfolio.models;

import java.math.BigDecimal;

import com.valtech.poc.mutualfundportfolio.entities.MutualFund;

public class MutualFundSchemeModel {

	private int id;
	private String schemeName;
	private String description;
	private BigDecimal netAssetValue;
	private MutualFund mutualFund;

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
