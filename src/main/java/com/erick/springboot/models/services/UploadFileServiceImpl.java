package com.erick.springboot.models.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileServiceImpl implements IUploadFileService {

	private final static String UPLOAD = "uploads";

	@Override
	public Resource cargar(String nombreFoto) throws MalformedURLException {

		Path rutaArchivo = getPath(nombreFoto);
		Resource recurso = null;

		recurso = new UrlResource(rutaArchivo.toUri());

		if (!recurso.exists() && !recurso.isReadable()) {
			throw new RuntimeException("Error no se pudo cargar la imagen");
		}

		return recurso;
	}

	@Override
	public String copiar(MultipartFile archivo) throws IOException {

		String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename().replace(" ", "");
		Path rutaArchivo = getPath(nombreArchivo);

		byte[] bytes = archivo.getBytes();
		Files.write(rutaArchivo, bytes);
		
		return nombreArchivo;
	}

	@Override
	public boolean eliminar(String nombreFoto) throws IOException {

		if (nombreFoto != null && nombreFoto.length() > 0) {
			
			Path rutaFotoAnterior = Paths.get("uploads").resolve(nombreFoto).toAbsolutePath();
			
			File archivoFotoAnterior = rutaFotoAnterior.toFile();
			if(archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
				Files.delete(rutaFotoAnterior);
				return true;
			}			
		}
		
		return false;
	}

	@Override
	public Path getPath(String nombreFoto) {

		return Paths.get(UPLOAD).resolve(nombreFoto).toAbsolutePath();
	}

}
