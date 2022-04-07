package com.group.contestback.services;


import com.group.contestback.models.Attempts;
import com.group.contestback.models.Scores;
import com.group.contestback.repositories.AttemptsRepo;
import com.group.contestback.repositories.ScoresRepo;
import com.group.contestback.responseTypes.ResultsResponse;
import com.group.contestback.responseTypes.Result;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ScoresServiceClass implements ScoresService{
    private final ScoresRepo scoresRepo;
    private final AttemptsRepo attemptsRepo;
    @Override
    public void addScore(Scores score) {
        scoresRepo.save(score);
    }

    @Override
    public List<Scores> getAllScores() {
        return scoresRepo.findAll();
    }

    @Override
    public void addAttempt(Attempts attempt) {
        attemptsRepo.save(attempt);
    }

    @Override
    public List<Attempts> getAllAttempts() {
        return attemptsRepo.findAll();
    }

    @Override
    public ResultsResponse checkSolution(Integer userId, Integer taskId, String solution) {
        ResultsResponse resultsResponse = new ResultsResponse();
        List<Attempts> attempts = attemptsRepo.findAllByTaskIdAndUserId(taskId,userId);
        Comparator<Attempts> comparator = (p1, p2) -> (int) (p2.getTime().getTime() - p1.getTime().getTime());
        attempts.sort(comparator);

        if(attempts.size() > 0 && (new Date().getTime() - attempts.get(0).getTime().getTime() < 60*1000)) {
            resultsResponse.setTimeout((int) (attempts.size()*60*1000 - (new Date().getTime() - attempts.get(0).getTime().getTime())));
            return resultsResponse;
        }

        //needed logic go check solution
        for(int i = 0; i < 2; ++i) {
            Result result = new Result("Test" + i,false);
            resultsResponse.getResults().add(result);
        }
        Boolean succeeded = false;
        Attempts attempt = new Attempts(userId, taskId, succeeded, solution);
        resultsResponse.setTimeout((attempts.size() + 1)*60*1000);
        attemptsRepo.save(attempt);

        return resultsResponse;
    }
}



