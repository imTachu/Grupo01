package com.uniandes.stampidia.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.uniandes.stampidia.utilities.Resultado;

@RestController
@RequestMapping(value = "/rest")
public class UploadController {	
	
	@Resource
    private Environment env;
	
	private static final String RESOURCES_PATH = "resources.path";
	
	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public Resultado darClientes() {
		Resultado ro = new Resultado();

		ro.setMensajeConsulta("El archivo subio correctamente!");

		return ro;
	}

	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public Resultado handleFileUpload(@RequestParam("name") String name, @RequestParam("file") MultipartFile file) {
		Resultado ro = new Resultado();

		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				File newFile = new File(env.getRequiredProperty(RESOURCES_PATH) + name);
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(newFile));
				stream.write(bytes);
				stream.close();
				ro.setMensajeConsulta("You successfully uploaded " + name + " into " + newFile.getName());
			} catch (Exception e) {
				ro.setMensajeConsulta("You failed to upload " + name + " => " + e.getMessage());
			}
		} else {
			ro.setMensajeConsulta("You failed to upload " + name + " because the file was empty.");
		}

		return ro;
	}

	@RequestMapping(value = "/uploadFiles", method = RequestMethod.POST)
	public Resultado handleMultipleFileUpload(@RequestParam("name") String name, @RequestParam("file") MultipartFile[] files) {
		Resultado ro = new Resultado();

		if (files.length != 0) {
			try {
				for (MultipartFile file : files) {
					byte[] bytes = file.getBytes();
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(name + "-uploaded")));
					stream.write(bytes);
					stream.close();
				}
				ro.setMensajeConsulta("You successfully uploaded " + files.length + " files");
			} catch (Exception e) {
				ro.setMensajeConsulta("You failed to upload " + name + " => " + e.getMessage());
			}
		} else {
			ro.setMensajeConsulta("You failed to upload " + name + " because the file was empty.");
		}

		return ro;
	}
}
