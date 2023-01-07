package com.locser.twitter;

import com.locser.twitter.models.ApplicationUser;
import com.locser.twitter.models.Role;
import com.locser.twitter.repositories.RoleRepository;

import com.locser.twitter.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class TwitterWithMeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwitterWithMeApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepo, UserService userService){
		return args -> {
			roleRepo.save(new Role(1L, "USER"));

//			ApplicationUser u= new ApplicationUser();
//			u.setFirstName("LocSer");
//			u.setLastName("Unknow");
//
//			userService.registerUser(u);
		};
	}

}
