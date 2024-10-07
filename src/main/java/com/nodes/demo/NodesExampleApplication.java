package com.nodes.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@SpringBootApplication
//@EntityScan(basePackageClasses = {
//		NodesExampleApplication.class,
//		Jsr310JpaConverters.class
//})
public class NodesExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(NodesExampleApplication.class, args);
	}

}
