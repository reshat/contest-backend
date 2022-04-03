package com.group.contestback.repositories;

import com.group.contestback.models.Attempts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttemptsRepo extends JpaRepository<Attempts, Integer> {
}
