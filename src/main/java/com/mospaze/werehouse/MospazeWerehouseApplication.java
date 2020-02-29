package com.mospaze.werehouse;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.web.config.EnableSpringDataWebSupport;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
@SpringBootApplication
@Configuration
@EnableSwagger2
@EnableSpringDataWebSupport
public class MospazeWerehouseApplication {

	public static void main(String[] args) {
		SpringApplication.run(MospazeWerehouseApplication.class, args);
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build().apiInfo(metaData());
	}

	private ApiInfo metaData() {
		ApiInfo apiInfo = new ApiInfo("Mospaze", "Mospaze engine for warehouse web", "1.0",
				"Terms of service",
				new Contact("Iman Khaeruddin", "https://mospaze.com/", "iman.khaerudin@mospaze.com"),
				"Apache License Version 2.0", "https://www.apache.org/licenses/LICENSE-2.0", Collections.emptyList());
		return apiInfo;
	}

}
