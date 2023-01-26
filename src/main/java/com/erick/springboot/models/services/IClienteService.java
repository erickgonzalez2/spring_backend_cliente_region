package com.erick.springboot.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.erick.springboot.models.entity.Cliente;
import com.erick.springboot.models.entity.Factura;
import com.erick.springboot.models.entity.Producto;
import com.erick.springboot.models.entity.Region;

public interface IClienteService {

	public List<Cliente> findAll();
	
	public Page<Cliente> findAll(Pageable pageable);
	
	public Cliente findById(Long id);
	
	public Cliente save(Cliente cliente);
	
	public void delete (Long id);
	
	public List<Region> findAllRegiones();
	
	public Factura findFacturaByid(Long id);
	
	public Factura saveFactura(Factura factura);
	
	public void deleteFacturaById(Long id);
	
	public List<Producto> findProductoByNombre(String nombre);
	
	
	
	
}
