package com.erick.springboot.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.erick.springboot.models.entity.Usuario;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
	

	private final Usuario usuario;
	
	public UserDetailsImpl(Usuario usuario) {
		super();
		this.usuario = usuario;
	}
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return Collections.emptyList();
	}

	@Override
	public String getPassword() {
		
		return usuario.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return usuario.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public String getNombre() {
		return usuario.getUsername();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
