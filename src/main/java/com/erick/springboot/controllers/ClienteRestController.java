package com.erick.springboot.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.erick.springboot.models.entity.Cliente;
import com.erick.springboot.models.services.IClienteService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class ClienteRestController {

	@Autowired
	private IClienteService clienteService;

	@GetMapping("/clientes")
	public List<Cliente> index() {
		return clienteService.findAll();
	}
	
	@GetMapping("/clientes/page/{page}")
	public Page<Cliente> index(@PathVariable Integer page) {
		Pageable pageable = PageRequest.of(page, 10);
		return clienteService.findAll(pageable);
	}

	@GetMapping("/clientes/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {

		Cliente cliente = null;
		Map<String, Object> response = new HashMap<>();

		try {
			cliente = clienteService.findById(id);
		} catch (DataAccessException e) {
			response.put("Mensaje", "error en la conexion");
			response.put("Exception", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (cliente == null) {
			response.put("Mensaje", "El id: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		else {
			return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
		}
	}

	@PostMapping("/clientes")
	public ResponseEntity<?> create(@Valid @RequestBody Cliente cliente, BindingResult result) {

		Cliente clienteNew = null;
		List<String> errores = new ArrayList<String>();

		Map<String, Object> response = new HashMap<>();

		Pattern pattern = Pattern.compile(
				"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

		boolean flag = true;

		if (cliente.getNombre() == null) {
			errores.add("El campo nombre no puede estar vacío");
			flag = false;
		}

		if (cliente.getApellido() == null) {
			errores.add("El campo apellido no puede estar vacío");
			flag = false;
		}

		if (cliente.getEmail() == null) {
			errores.add("El campo email no puede estar vacío");
			flag = false;
		}

		else {
			Matcher mather = pattern.matcher(cliente.getEmail());
			if (mather.find() == false) {
				errores.add("El email ingresado es inválido");
				flag = false;
			}
		}

		if (!flag) {
			response.put("Errores", errores);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);

		}

		try {
			clienteNew = clienteService.save(cliente);
		} catch (DataAccessException e) {
			response.put("Mensaje", "error en la conexion");
			response.put("Exception", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("Mensaje", "Registro añadido con exito");
		response.put("Cliente", clienteNew);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("/clientes/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Cliente cliente, BindingResult result, @PathVariable Long id) {

		Cliente clienteActual = null;

		Map<String, Object> response = new HashMap<>();

		List<String> errores = new ArrayList<String>();

		Pattern pattern = Pattern.compile(
				"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

		boolean flag = true;

		if (cliente.getNombre() == null) {
			errores.add("El campo nombre no puede estar vacío");
			flag = false;
		}

		if (cliente.getApellido() == null) {
			errores.add("El campo apellido no puede estar vacío");
			flag = false;
		}

		if (cliente.getEmail() == null) {
			errores.add("El campo email no puede estar vacío");
			flag = false;
		}

		else {
			Matcher mather = pattern.matcher(cliente.getEmail());
			if (mather.find() == false) {
				errores.add("El email ingresado es inválido");
				flag = false;
			}
		}

		if (!flag) {
			response.put("Errores", errores);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);

		}

		try {
			clienteActual = clienteService.findById(id);
		} catch (DataAccessException e) {
			response.put("Mensaje", "error en la conexion");
			response.put("Exception", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (clienteActual == null) {
			response.put("Mensaje", "El id: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		clienteActual.setApellido(cliente.getApellido());
		clienteActual.setNombre(cliente.getNombre());
		clienteActual.setEmail(cliente.getEmail());

		try {
			clienteService.save(clienteActual);
		} catch (DataAccessException e) {
			response.put("Mensaje", "error en la conexion");
			response.put("Exception", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("Mensaje", "Registro actualizado con exito");
		response.put("Cliente:", clienteActual);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/clientes/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> delete(@PathVariable Long id) {

		Map<String, Object> response = new HashMap<>();

		try {
			clienteService.delete(id);
		} catch (DataAccessException e) {
			response.put("Mensaje", "error en la conexion");
			response.put("Exceptin", e.getClass());
		}

		response.put("Mensaje", "Cliente eliminado con exito");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}

	
	public ResponseEntity<?> upload(@RequestParam("archivo")MultipartFile archivo, @RequestParam("id") Long id){

		Map<String,Object> response = new HashMap<>();
		
		Cliente cliente = clienteService.findById(id);
		
		if(!archivo.isEmpty()) {
			String nombreArchivo = archivo.getOriginalFilename();
			Path rutaArchivo = Paths.get("upload").resolve(nombreArchivo).toAbsolutePath();
			
			try {
				Files.copy(archivo.getInputStream(), rutaArchivo);
			} catch (IOException e) {				// 
				e.printStackTrace();
			}
			
			cliente.setFoto(nombreArchivo);
			
			clienteService.save(cliente);
			
			response.put("cliente", cliente);
			response.put("Mensaje", "Archivo subido correctamente "+rutaArchivo);
		}
		
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
		
	}
	
}
