package com.christ.Salud2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@ComponentScan
public class Salud2Application {

	public static void main(String[] args) {
		SpringApplication.run(Salud2Application.class, args);
	}

}
