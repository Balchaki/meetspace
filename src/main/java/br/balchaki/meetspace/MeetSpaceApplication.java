package br.balchaki.meetspace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class MeetSpaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeetSpaceApplication.class, args);
	}



}
