package com.npd.npdstreaming;

import com.npd.npdstreaming.userInteraction.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NpdStreamingApplication {

	public static void main(String[] args) {
		SpringApplication.run(NpdStreamingApplication.class, args);
	}

}
