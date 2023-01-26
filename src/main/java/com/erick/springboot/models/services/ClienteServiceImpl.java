package com.erick.springboot.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import com.erick.springboot.models.dao.IClienteDao;
import com.erick.springboot.models.dao.IFacturaDao;
import com.erick.springboot.models.dao.IProductoDao;
import com.erick.springboot.models.entity.Cliente;
import com.erick.springboot.models.entity.Factura;
import com.erick.springboot.models.entity.Producto;
import com.erick.springboot.models.entity.Region;


@Service 
public class ClienteServiceImpl implements IClienteService {

	@Autowired
	private IClienteDao clienteDao;
	
	@Autowired IProductoDao productoDao;
	
	@Autowired
	private IFacturaDao facturaDao;
	
	@Override
	@Transactional (readOnly = true)
	public List<Cliente> findAll() {		
		return (List<Cliente>) clienteDao.findAll();
		}
	
	@Override
	@Transactional (readOnly = true)
	public Page<Cliente> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return clienteDao.findAll(pageable);
	}


	@Override
	@Transactional (readOnly = true)
	public Cliente findById(Long id) {
		// TODO Auto-generated method stub
		return clienteDao.findById(id).orElse(null);
	}

	@Override
	@Transactional 
	public Cliente save(Cliente cliente) {
		// TODO Auto-generated method stub
		return clienteDao.save(cliente);
	}

	@Override
	@Transactional 
	public void delete(Long id) {
		// TODO Auto-generated method stub
		clienteDao.deleteById(id);
	}

	@Override
	@Transactional (readOnly = true)
	public List<Region> findAllRegiones() {

		return clienteDao.findAllRegiones();
	}

	@Override
	@Transactional (readOnly = true)
	public Factura findFacturaByid(Long id) {
	
		return facturaDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Factura saveFactura(Factura factura) {

		return facturaDao.save(factura);
	}

	@Override
	@Transactional
	public void deleteFacturaById(Long id) {
		
		facturaDao.deleteById(id);
		
	}

	@Override
	@Transactional (readOnly = true)	
	public List<Producto> findProductoByNombre(String nombre) {
		return productoDao.findProductoByNombre(nombre);
	}
}
