package com.valtech.poc.mutualfundportfolio.entities;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class MutualFund {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(length = 20, nullable = false)
	private String type;
	@Column(length = 200, nullable = false)
	private String description;
	@OneToMany(targetEntity = MutualFundScheme.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mutualFund")
	@JsonIgnore
	private Set<MutualFundScheme> mutualFundSchemes;

	public void addSchemes(MutualFundScheme scheme) {
		if (getMutualFundSchemes() == null) {
			HashSet<MutualFundScheme> schemeset = new HashSet<>();
			setMutualFundSchemes(schemeset);
		}
		getMutualFundSchemes().add(scheme);
		scheme.setMutualFund(this);
	}

	public MutualFund() {
	}

	public MutualFund(String type, String description) {
		this.type = type;
		this.description = description;
	}

	public MutualFund(int id, String type, String description) {
		this.id = id;
		this.type = type;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<MutualFundScheme> getMutualFundSchemes() {
		return mutualFundSchemes;
	}

	public void setMutualFundSchemes(Set<MutualFundScheme> mutualFundSchemes) {
		this.mutualFundSchemes = mutualFundSchemes;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MutualFund other = (MutualFund) obj;
		return id == other.id && Objects.equals(type, other.type);
	}

	@Override
	public String toString() {
		return "MutualFund [id=" + id + ", type=" + type + ", description=" + description + ", mutualFundSchemes=" + mutualFundSchemes + "]";
	}
}
