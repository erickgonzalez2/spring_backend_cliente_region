package com.erick.springboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.erick.springboot.models.dao.IUsuarioDao;
import com.erick.springboot.models.entity.Usuario;

@Service
public class UserDetailServiceImpl implements UserDetailsService{

	
	
	@Autowired
	IUsuarioDao usuarioDao;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	Usuario usuario =	usuarioDao.findOneByEmail(email)
		.orElseThrow(() -> new UsernameNotFoundException("El usuario con email "+email+" no existe"));
	return	 new UserDetailsImpl(usuario);		
	}
}
