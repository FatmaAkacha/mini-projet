package spring.jpa;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import spring.jpa.model.Employe;
import spring.jpa.repository.EmployeRepository;

@SpringBootApplication
public class MiniProjetApplication {
	static EmployeRepository employeRepos ;
	public static void main(String[] args) {
		ApplicationContext contexte =

				SpringApplication.run(MiniProjetApplication.class, args);
	}}
