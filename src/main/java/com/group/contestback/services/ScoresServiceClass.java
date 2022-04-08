package com.group.contestback.services;


import com.group.contestback.models.*;
import com.group.contestback.repositories.*;
import com.group.contestback.responseTypes.GroupCoursesScoresResponse;
import com.group.contestback.responseTypes.GroupStudents;
import com.group.contestback.responseTypes.ResultsResponse;
import com.group.contestback.responseTypes.Result;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    private final RolesRepo rolesRepo;
    private final TasksRepo tasksRepo;
    private final TaskTypesRepo taskTypesRepo;
    private final TaskCoursesRepo taskCoursesRepo;
    private final GroupsRepo groupsRepo;
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
    public ResultsResponse checkSolution(Integer taskId, String solution) {
        ResultsResponse resultsResponse = new ResultsResponse();
        Integer userId = appUserRepo.findByLogin(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()).getId();
        Tasks task = tasksRepo.findById(taskId).get();
        List<Attempts> attempts = attemptsRepo.findAllByTaskIdAndUserId(taskId,userId);
        String taskType = taskTypesRepo.getById(task.getTaskTypeId()).getName();

        if((attempts.size() > 0 && taskType.equals("SIMPLE_TASK")) || (task.getDeadLine().getTime() - new Date().getTime() < 0)) {
            resultsResponse.setDeadlinePassed(true);
            return resultsResponse;
        }
        Comparator<Attempts> comparator = (p1, p2) -> (int) (p2.getTime().getTime() - p1.getTime().getTime());
        attempts.sort(comparator);


        if(attempts.size() > 0 && (new Date().getTime() - attempts.get(0).getTime().getTime() < attempts.size() *60*1000)) {
            resultsResponse.setTimeout((int) (attempts.size()*60*1000 - (new Date().getTime() - attempts.get(0).getTime().getTime())));
            return resultsResponse;
        }

        //needed logic go check solution
        boolean succeeded = false;
        Scores score;

        // SIMPLE_TASK - only 1 attempt
        // SQL_TASK - attempts restricted by time, but results are shown immediately
        // MANUAL_TASK - attempts restricted by time
        if(taskType.equals("SIMPLE_TASK")) {
            if(task.getSolution().equals(solution)) {
                succeeded = true;
                score = new Scores(userId, taskId, 5, 1, "");
            } else {
                score = new Scores(userId, taskId, 1, 1, "");
            }
            resultsResponse.setDeadlinePassed(true);
            scoresRepo.save(score);
        } else if(taskType.equals("SQL_TASK")) {
            boolean noErrors = true;
            for(int i = 0; i < 2; ++i) {
                noErrors = false;
                Result result = new Result("Test" + i,false);
                resultsResponse.getResults().add(result);
            }
            if(noErrors) {
                succeeded = true;
                score = new Scores(userId, taskId, 5, 1, solution);
            } else {
                score = new Scores(userId, taskId, 1, 1, solution);
            }
            resultsResponse.setTimeout((attempts.size() + 1)*60*1000);
            scoresRepo.save(score);
        } else {
            resultsResponse.setTimeout((attempts.size() + 1)*60*1000);
        }

        Attempts attempt = new Attempts(userId, taskId, succeeded, solution);
        attemptsRepo.save(attempt);

        return resultsResponse;
    }

    @Override
    public List<Scores> getStudentScores() {
        List<Scores> allScores = scoresRepo.findAllByUserId(appUserRepo.findByLogin(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()).getId());
        List<Scores> lastScores = new ArrayList<>();
        for (Scores scores : allScores) {
            Integer taskId = scores.getTaskId();
            Date date = scores.getDate();
            for (int k = 0; k < lastScores.size(); ++k) {
                Scores lastScore = lastScores.get(k);
                if (lastScore.getTaskId().equals(taskId) && ((lastScore.getDate().getTime() - date.getTime()) < 0)) {
                    lastScores.remove(lastScore);
                }
            }
            lastScores.add(scores);

        }
        return lastScores;
    }

    @Override
    public List<Scores> getGroupScoresForTask(Integer groupId, Integer taskId) {
        List<Scores> scores = scoresRepo.findAllScoresByTaskAndGroup(groupId, taskId);
        List<AppUser> users = appUserRepo.findAllByGroupId(groupId);
        List<Scores> result = new ArrayList<>();
        users.forEach(appUser -> {
            int resSize = result.size();
            scores.forEach(scores1 -> {
                if(appUser.getId().equals(scores1.getUserId())){
                    result.add(scores1);
                }
            });
            if(resSize == result.size() && rolesRepo.getById(appUser.getRoleId()).getName().equals("ROLE_USER")){
                result.add(new Scores(null,appUser.getId(),taskId,null,null,null,null));
            }
        });
        return result;
    }

    @Override
    public List<Attempts> getStudentAttemptsOnTask(Integer taskId) {
        log.info(appUserRepo.findByLogin(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()).getId().toString());
        log.info(taskId.toString());
        return attemptsRepo.findAllByTaskIdAndUserId(taskId,appUserRepo.findByLogin(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()).getId());
    }

    @Override
    public GroupCoursesScoresResponse getGroupScoresForCourse(Integer groupId, Integer courseId) {
        List<TaskCourses> taskCourses = taskCoursesRepo.findAllByCourseId(courseId);
        List<AppUser> users = appUserRepo.findAllByGroupId(groupId);
        GroupCoursesScoresResponse groupCoursesScoresResponse = new GroupCoursesScoresResponse();

            for(AppUser user: users) {
                List<Scores> userScores = new ArrayList<>();
                for(TaskCourses taskCourse: taskCourses) {
                    List<Scores> scores = scoresRepo.findAllByUserIdAndTaskId(user.getId(), taskCourse.getTaskId());
                    if(scores.size() > 0){
                        Comparator<Scores> comparator = (p1, p2) -> (int) (p2.getDate().getTime() - p1.getDate().getTime());
                        scores.sort(comparator);
                        userScores.add(scores.get(0));
                    } else {
                        Scores nullScore = new Scores(user.getId(),taskCourse.getTaskId(),null,null,null);
                        userScores.add(nullScore);
                    }
                }
                groupCoursesScoresResponse.addUser(userScores, user.getId(), user.getLogin(), user.getFirstName(), user.getLastName(),user.getMiddleName(), user.getEmail(), user.getRoleId(),user.getGroupId());
            }

        return groupCoursesScoresResponse;
    }

    @Override
    public List<GroupStudents> getAllManualAttempts() {
        List<GroupStudents> groupStudents = new ArrayList<>();
        List<Groups> groups = groupsRepo.findAll();
        List<Tasks> manualTasks = tasksRepo.findAllByTaskTypeId(3);
        List<Integer> manualTasksIds = new ArrayList<>();
        for (Tasks task: manualTasks) {
            manualTasksIds.add(task.getId());
        }

        for (Groups group: groups) {
            GroupStudents groupStud = new GroupStudents();
            groupStud.setGroups(group);
            List<AppUser> users = appUserRepo.findAllByGroupId(group.getId());
            for(AppUser user: users) {
                List<Attempts> attempts = attemptsRepo.findByTaskUserMaxTime(user.getId());
                List<Attempts> manualLastAttempts = new ArrayList<>();
                for(Attempts attempt: attempts) {
                    if(manualTasksIds.contains(attempt.getTaskId())) {
                        manualLastAttempts.add(attempt);
                    }
                }
                groupStud.addUser(user, manualLastAttempts);
            }
            groupStudents.add(groupStud);
        }
        return groupStudents;
    }
}



