package com.group.contestback.repositories;

import com.group.contestback.models.Roles;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RolesRepo extends JpaRepository<Roles,Integer> {
    List<Roles> findAllById(Integer Id);
    Roles findByName(String name);
}
