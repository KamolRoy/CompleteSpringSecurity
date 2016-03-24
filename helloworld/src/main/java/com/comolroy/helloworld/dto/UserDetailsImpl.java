package com.comolroy.helloworld.dto;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.comolroy.helloworld.entities.User;
import com.comolroy.helloworld.entities.User.Role;

/*
 * This class is the principal variable of <c:authentication> tag
 * The class is called from MyUtil's getSessionUser() method.
 */
public class UserDetailsImpl implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6084969699837668538L;

	private User user;
	
	public UserDetailsImpl(User user){
		this.user=user;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>(user.getRoles().size()+1);
		
		for (Role role : user.getRoles()) {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
		}
		
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		
		for (GrantedAuthority grantedAuthority : authorities) {
			System.out.println("Current user " + user.getName() + " has authority " + grantedAuthority);
		}
		
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
