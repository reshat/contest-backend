package com.group.contestback.repositories;

import com.group.contestback.models.Scores;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoresRepo extends JpaRepository<Scores, Integer>{
}
