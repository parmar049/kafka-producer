package com.twitter.kafka.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@SpringBootApplication
//@EnableSwagger2
public class TweetProducerApplication {

	public static void main(String[] args) {

		SpringApplication.run(TweetProducerApplication.class, args);
	}

	/*@Bean
	public Docket swaggerConfiguration() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.twitter.kafka"))
				.paths(PathSelectors.any())
				.build().apiInfo(getAPIInfo());
	}

	private ApiInfo getAPIInfo() {
		return new ApiInfo("Twitter tweets pushing", "APIs", "V1.0", "https://localhost:8080",
				new Contact("Karan Parmar", "https://localhost:8080", "karan.parmar049@gmail.com")
				, "API Lisence", "", Collections.emptySet());

	}*/

}
