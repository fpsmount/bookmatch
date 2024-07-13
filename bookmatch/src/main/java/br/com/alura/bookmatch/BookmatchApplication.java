package br.com.alura.bookmatch;

import br.com.alura.bookmatch.PRINCIPAL.Principal;
import br.com.alura.bookmatch.REPOSITORY.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(BookmatchApplication.class, args);
	}

	@Autowired
	private AutorRepository repositorio;

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repositorio);
		principal.exibeMenu();
	}
}
