package com.npd.npdstreaming;

import com.npd.npdstreaming.UserInteraction.Menu;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NpdStreamingApplication implements CommandLineRunner {

	private final Menu menu;
	public NpdStreamingApplication(Menu menu) {
		this.menu = menu;
	}

	public static void main(String[] args) {
		SpringApplication.run(NpdStreamingApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		menu.showMenu();
	}
}
