package com.megvii;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableConfigurationProperties
@ComponentScan
@SpringBootApplication
@EnableScheduling
public class PhotoDataDownloadApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotoDataDownloadApplication.class, args);
	}
}
