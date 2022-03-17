package com.group.contestback.repositories;

import com.group.contestback.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepo extends JpaRepository<AppUser, Long> {
    //AppUser findByUsername(String username);
    Optional<AppUser> findById(Integer id);
    AppUser findByLogin(String login);
}
