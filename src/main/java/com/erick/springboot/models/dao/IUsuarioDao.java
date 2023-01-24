package com.erick.springboot.models.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.erick.springboot.models.entity.Usuario;


public interface IUsuarioDao extends JpaRepository <Usuario, Long>{

	
	
	Optional<Usuario> findOneByEmail(String email);
}
