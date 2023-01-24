package ru.itsrv23.terminal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TerminalPaymentsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TerminalPaymentsApplication.class, args);
	}
}
