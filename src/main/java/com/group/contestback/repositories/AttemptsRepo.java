package com.group.contestback.repositories;

import com.group.contestback.models.Attempts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttemptsRepo extends JpaRepository<Attempts, Integer> {
    List<Attempts> findAllByTaskIdAndUserId(Integer taskId, Integer userId);
}
