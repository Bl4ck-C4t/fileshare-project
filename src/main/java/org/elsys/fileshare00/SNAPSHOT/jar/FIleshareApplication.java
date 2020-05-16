package org.elsys.fileshare00.SNAPSHOT.jar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@SpringBootApplication
public class FIleshareApplication {

	public static void main(String[] args) {
		SpringApplication.run(FIleshareApplication.class, args);
	}


}



