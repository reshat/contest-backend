package com.group.contestback.services;

import com.group.contestback.models.Attempts;
import com.group.contestback.models.Scores;

import java.util.List;

public interface ScoresService {
    void addScore(Scores score);
    List<Scores> getAllScores();
    void addAttempt(Attempts attempt);
    List<Attempts> getAllAttempts();
}
