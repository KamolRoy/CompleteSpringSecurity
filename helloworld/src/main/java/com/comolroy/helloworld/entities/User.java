package com.comolroy.helloworld.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.comolroy.helloworld.util.MyUtil;

@Entity
@Table(name = "usr", indexes = { @Index(columnList = "email", unique = true),
		
@Index(columnList = "forgotpasswordcode", unique=true)})
public class User {

	public static final int EMAIL_MAX = 250;
	public static final int NAME_MAX = 50;
	public static final int PASSWORD_MAX = 16;
	public static final String EMAIL_PATTERN = "[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
	public static final int RANDOM_LEANGTH = 16;

	public static enum Role {
		UNVERIFIED, BLOCKED, ADMIN, DBA
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false, length = EMAIL_MAX)
	private String email;

	@Column(nullable = false, length = NAME_MAX)
	private String name;

	@Column(nullable = false)
	private String password;

	@Column(length = RANDOM_LEANGTH, name = "verification_code")
	private String verificationcode;

	@Column(length = RANDOM_LEANGTH)
	private String forgotpasswordcode;

	/*
	 * To load it together with the rest of the fields (i.e. eagerly) or to load
	 * it on-demand (i.e. lazily) Also, following represent another table name
	 * 'user_roles' with FK user_id
	 */
	// @ElementCollection(fetch = FetchType.EAGER)

	@Column(name = "roles")
	@CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
	@Enumerated(EnumType.STRING)
	@ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
	private Set<Role> roles = new HashSet<>();

	/*
	 * @Column private String roles;
	 */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVarificationCode() {
		return verificationcode;
	}

	public void setVarificationCode(String varificationCode) {
		this.verificationcode = varificationCode;
	}

	/*
	 * public String getRoles() { return roles; }
	 * 
	 * public void setRoles(String roles) { this.roles = roles; }
	 */

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getForgotPasswordCode() {
		return forgotpasswordcode;
	}

	public void setForgotPasswordCode(String forgotPasswordCode) {
		this.forgotpasswordcode = forgotPasswordCode;
	}
	
	public boolean isAdmin() {
		return roles.contains(Role.ADMIN);
	}
	
	public boolean isEditable(){
		User loggedIn = MyUtil.getSessionUser();
		if(loggedIn == null)
			return false;
		return loggedIn.isAdmin() || loggedIn.getId() == this.id;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", name=" + name + ", password=" + password
				+ ", varificationCode=" + verificationcode + ", roles=" + roles + "]";
	}

	

}
