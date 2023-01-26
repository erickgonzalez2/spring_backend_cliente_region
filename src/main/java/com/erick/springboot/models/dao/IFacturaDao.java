package com.erick.springboot.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.erick.springboot.models.entity.Factura;

public interface IFacturaDao extends CrudRepository<Factura, Long> {

}
