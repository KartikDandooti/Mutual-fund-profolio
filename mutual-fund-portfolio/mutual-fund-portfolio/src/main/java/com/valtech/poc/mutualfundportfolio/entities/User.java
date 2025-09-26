package com.valtech.poc.mutualfundportfolio.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(length = 20, nullable = false)
	private String firstName;
	@Column(length = 20, nullable = false)
	private String lastName;
	@Column(nullable = false)
	private int age;
	@Column(nullable = false)
	private long phoneNumber;
	@Column(unique = true, length = 50, nullable = false)
	private String email;
	@Column(nullable = false)
	private String password;
	@Column(unique = true, length = 10, nullable = false)
	private String portfolioNumber;
	@Column(nullable = false)
	private boolean enabled;
	@Column(nullable = false)
	private LocalDateTime registeredDate;
	private LocalDateTime modifiedDate;
	@Column(length = 10, nullable = false)
	private String role;
	@OneToMany(targetEntity = Transaction.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
	private Set<Transaction> transactions;
	@OneToMany(targetEntity = UserScheme.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
	private Set<UserScheme> userSchemes;

	public void addUserSchemes(UserScheme userScheme) {
		if (getUserSchemes() == null) {
			HashSet<UserScheme> schemeSet = new HashSet<>();
			setUserSchemes(schemeSet);
		}
		getUserSchemes().add(userScheme);
		userScheme.setUser(this);
	}

	public void addTransactions(Transaction transaction) {
		if (getTransactions() == null) {
			HashSet<Transaction> transactionset = new HashSet<>();
			setTransactions(transactionset);
		}
		getTransactions().add(transaction);
		transaction.setUser(this);
	}

	public User() {
	}

	public User(String firstName, String lastName, int age, long phoneNumber, String email, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.password = password;
	}

	public User(String firstName, String lastName, int age, long phoneNumber, String email, String password, String portfolioNumber, boolean enabled, LocalDateTime registeredDate, String role) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.password = password;
		this.portfolioNumber = portfolioNumber;
		this.enabled = enabled;
		this.registeredDate = registeredDate;
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPortfolioNumber() {
		return portfolioNumber;
	}

	public void setPortfolioNumber(String portfolioNumber) {
		this.portfolioNumber = portfolioNumber;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public LocalDateTime getRegisteredDate() {
		return registeredDate;
	}

	public void setRegisteredDate(LocalDateTime registeredDate) {
		this.registeredDate = registeredDate;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Set<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Set<UserScheme> getUserSchemes() {
		return userSchemes;
	}

	public void setUserSchemes(Set<UserScheme> userSchemes) {
		this.userSchemes = userSchemes;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, id, portfolioNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(email, other.email) && id == other.id && Objects.equals(portfolioNumber, other.portfolioNumber);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", age=" + age + ", phoneNumber=" + phoneNumber + ", email=" + email + ", password=" + password
		      + ", portfolioNumber=" + portfolioNumber + ", enabled=" + enabled + ", registeredDate=" + registeredDate + ", modifiedDate=" + modifiedDate + ", role=" + role + ", transactions="
		      + transactions + ", userSchemes=" + userSchemes + "]";
	}
}