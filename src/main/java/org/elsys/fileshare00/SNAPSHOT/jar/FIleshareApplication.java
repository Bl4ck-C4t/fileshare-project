package org.elsys.fileshare00.SNAPSHOT.jar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.File;
import java.util.Arrays;
import java.util.Properties;

@SpringBootApplication
public class FIleshareApplication {

	public static void main(String[] args) {
		FIleshareApplication.setupUsersDir();
		SpringApplication.run(FIleshareApplication.class, args);
	}

	private static void setupUsersDir(){
		File fl = new File(".");
		String[] list = fl.list();

		if(!Arrays.asList(list).contains("UsersFiles")){
			boolean result = new File("./UsersFiles").mkdir();
			if(result){
				System.out.println("Created users files directory");
			}
			else{
				System.out.println("Error when attempting to setup users dir.");
			}

		}

	}


}



