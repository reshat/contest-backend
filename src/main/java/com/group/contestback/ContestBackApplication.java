package com.group.contestback;

import com.group.contestback.models.AppUser;
import com.group.contestback.models.Role;
import com.group.contestback.services.AppUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class ContestBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContestBackApplication.class, args);
    }

    @Bean
    CommandLineRunner run(AppUserService userService) {
        return args -> {
            if(userService.getUsers().size() == 0) {
                userService.saveRole(new Role(null, "ROLE_USER"));
                userService.saveRole(new Role(null, "ROLE_ADMIN"));

                userService.saveAppUser(new AppUser(null, "Ivan","ivan","1234", new ArrayList<>()));
                userService.saveAppUser(new AppUser(null, "Polina","polina","1234", new ArrayList<>()));
                userService.saveAppUser(new AppUser(null, "Andrei","andrei","1234", new ArrayList<>()));

                userService.addRoleToUser("ivan","ROLE_USER");
                userService.addRoleToUser("polina","ROLE_ADMIN");
            }
        };
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
