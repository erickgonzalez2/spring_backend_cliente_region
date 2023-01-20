package com.erick.springboot.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erick.springboot.models.entity.Cliente;

public interface IClienteDao extends JpaRepository <Cliente, Long>{

}
