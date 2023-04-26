package com.quisofka.questions;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(title = "Quisofka",
				version = "1.0",
				description = "Documentation created for Quisofka support API in charge of providing questions"
		),
		servers = {
		@Server(url = "http://localhost:8080/"),
		@Server(url = "https://quisofka-b-production-c47e.up.railway.app/")
})
public class QuestionsApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuestionsApplication.class, args);
	}

}
