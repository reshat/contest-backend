package com.group.contestback.services;

import com.group.contestback.models.Scores;

import java.util.List;

public interface ScoresService {
    void addScore(Scores score);
    List<Scores> getAllScores();
}
