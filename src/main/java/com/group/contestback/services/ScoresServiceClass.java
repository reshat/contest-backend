package com.group.contestback.services;


import com.group.contestback.models.AppUser;
import com.group.contestback.models.Attempts;
import com.group.contestback.models.Scores;
import com.group.contestback.repositories.AppUserRepo;
import com.group.contestback.repositories.AttemptsRepo;
import com.group.contestback.repositories.ScoresRepo;
import com.group.contestback.responseTypes.ResultsResponse;
import com.group.contestback.responseTypes.Result;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.ArrayList;
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
    private final AppUserRepo appUserRepo;
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

    @Override
    public List<Scores> getStudentScores() {
        return scoresRepo.findAllByUserId(appUserRepo.findByLogin( SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()).getId());
    }

    @Override
    public List<Scores> getGroupScoresForTask(Integer groupId, Integer taskId) {
        List<Scores> scores = scoresRepo.findAllScoresByTaskAndGroup(groupId, taskId);
        List<AppUser> users = appUserRepo.findAllByGroupId(groupId);
        List<Scores> result = new ArrayList<>();
        users.forEach(appUser -> {
            Integer resSize = result.size();
            scores.forEach(scores1 -> {
                if(appUser.getId() == scores1.getUserId()){
                    result.add(scores1);
                }
            });
            if(resSize == result.size()){
                result.add(new Scores(null,appUser.getId(),taskId,null,null,null,null));
            }
        });
        return result;
    }
}



