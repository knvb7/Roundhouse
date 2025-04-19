package com.SportNexus.Athletiverse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Athletiverse API",
        version = "1.0",
        description = "Athletiverse – The Ultimate Business & Networking Platform for Athletes, Fans, and Sponsors!",
        contact = @Contact(
            name = "SportNexus",
            email = "support@sportnexus.com"
        ),
        license = @License(
            name = "License",
            url = "https://sportnexus.com/license"
        )
    )
)
public class AthletiverseApplication {

	public static void main(String[] args) {
		SpringApplication.run(AthletiverseApplication.class, args);
	}

}
