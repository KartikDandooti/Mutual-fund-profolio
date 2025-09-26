package com.valtech.poc.mutualfundportfolio.entities;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class UserSchemeId {
	@ManyToOne(targetEntity = User.class, cascade = { CascadeType.MERGE, CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", referencedColumnName = "id")
	@JsonIgnore
	private User user;
	@ManyToOne(targetEntity = MutualFundScheme.class, cascade = { CascadeType.MERGE, CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "schemeId", referencedColumnName = "id")
	@JsonIgnore
	private MutualFundScheme scheme;

	public UserSchemeId() {
	}

	public UserSchemeId(User user, MutualFundScheme scheme) {
		this.user = user;
		this.scheme = scheme;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public MutualFundScheme getScheme() {
		return scheme;
	}

	public void setScheme(MutualFundScheme scheme) {
		this.scheme = scheme;
	}

	@Override
	public int hashCode() {
		return Objects.hash(scheme, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserSchemeId other = (UserSchemeId) obj;
		return Objects.equals(scheme, other.scheme) && Objects.equals(user, other.user);
	}
}