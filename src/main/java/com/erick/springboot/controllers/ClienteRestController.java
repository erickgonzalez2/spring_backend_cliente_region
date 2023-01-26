package com.erick.springboot.controllers;

import java.io.IOException;
import java.net.MalformedURLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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
import com.erick.springboot.models.entity.Region;
import com.erick.springboot.models.services.IClienteService;
import com.erick.springboot.models.services.IUploadFileService;

@CrossOrigin(origins = { "http://localhost:4200","*" })
@RestController
@RequestMapping("/api")
public class ClienteRestController {

	@Autowired
	private IClienteService clienteService;

	@Autowired
	private IUploadFileService uploadService;


	@GetMapping("/clientes")
	public List<Cliente> index() {
		return clienteService.findAll();
	}

	@GetMapping("/clientes/page/{page}")
	public Page<Cliente> index(@PathVariable Integer page) {
		Pageable pageable = PageRequest.of(page, 10);
		return clienteService.findAll(pageable);
	}

	@GetMapping("/clientes/id/{id}")
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

	@PutMapping("/clientes/id/{id}")
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
		clienteActual.setRegion(cliente.getRegion());

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

	@DeleteMapping("/clientes/id/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> delete(@PathVariable Long id) {

		Map<String, Object> response = new HashMap<>();

		try {
			Cliente cliente = clienteService.findById(id);

			String nombreFotoAnterior = cliente.getFoto();

			uploadService.eliminar(nombreFotoAnterior);

			clienteService.delete(id);

		} catch (DataAccessException | IOException e) {
			response.put("Mensaje", "error en la conexion");
			response.put("Exception", e.getClass());
		}

		response.put("Mensaje", "Cliente eliminado con exito");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}

	@PostMapping("/clientes/upload")
	public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id) {

		Map<String, Object> response = new HashMap<>();

		Cliente cliente = clienteService.findById(id);

		String nombreArchivo = "";

		if (!archivo.isEmpty()) {

			try {

				nombreArchivo = uploadService.copiar(archivo);

				String nombreFotoAnterior = cliente.getFoto();

				uploadService.eliminar(nombreFotoAnterior);

				cliente.setFoto(nombreArchivo);
				clienteService.save(cliente);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				response.put("Error", "Error al subir el archivo");
				response.put("Exception", e.getMessage().concat(":") + e.getCause());
			}
		} else {

			response.put("Error", "El archivo esta vacío");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		response.put("Mensaje", "Imagen subida correctamente ");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

	}

	@GetMapping("/uploads/img/{nombreFoto:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String nombreFoto) {

		Resource recurso = null;

		try {
			recurso = uploadService.cargar(nombreFoto);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpHeaders cabecera = new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"");

		return new ResponseEntity<Resource>(recurso, cabecera, HttpStatus.OK);
	}

	
	@GetMapping("/clientes/regiones")
	public List<Region>listarRegiones(){

		return clienteService.findAllRegiones();
		
	}
}
