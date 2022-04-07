package com.group.contestback.repositories;

import com.group.contestback.models.Scores;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScoresRepo extends JpaRepository<Scores, Integer>{
    List<Scores> findAllByUserId(Integer userId);
}
