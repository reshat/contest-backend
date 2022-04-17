package com.group.contestback.services;


import com.group.contestback.models.*;
import com.group.contestback.repositories.*;
import com.group.contestback.responseTypes.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.*;
import java.util.*;
import java.util.Date;

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
    private final SolutionVariantsRepo solutionVariantsRepo;

    @Value("${spring.datasource.url}")
    private String dataSourceURL;

    @Value("${user.sql.username}")
    private String username;

    @Value("${user.sql.password}")
    private String userPass;

    @Value("${open.table.schema}")
    private String openSchema;

    @Value("${closed.table.schema}")
    private String closedSchema;

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

    private List<List<String>> runSQLQueryUser(String solution, String schema) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");

        Properties props = new Properties();
        props.setProperty("user",username);
        props.setProperty("password",userPass);
        Connection connection = DriverManager.getConnection(dataSourceURL, props);

        log.info("schema " + connection.getSchema());

        Statement statement = connection.createStatement();


        log.info("SET search_path TO hiddentests; " + solution);
        log.info(String.valueOf(statement.executeUpdate("SET search_path TO " + schema)));
        ResultSet resultSet = statement.executeQuery(solution);

        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        final int columnCount = resultSetMetaData.getColumnCount();

        List<List<String>> result = new ArrayList<>();

        List<String> columnNameRow = new ArrayList<>();

        for (int i = 1; i <= columnCount; ++i) {
            columnNameRow.add(resultSetMetaData.getColumnName(i));
        }
        result.add(columnNameRow);

        while (resultSet.next()) {
            List<String> resRow = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                resRow.add(resultSet.getObject(i).toString());
            }
            result.add(resRow);
        }
        statement.close();
        connection.close();
        return result;
    }

    @Override
    public ResultsResponse checkSQLSolution(Integer taskId, String solution) {
        ResultsResponse resultsResponse = new ResultsResponse();
        Integer userId = appUserRepo.findByLogin(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()).getId();
        Tasks task = tasksRepo.findById(taskId).get();
        List<Attempts> attempts = attemptsRepo.findAllByTaskIdAndUserId(taskId,userId);
        String taskType = taskTypesRepo.getById(task.getTaskTypeId()).getName();

        if(!(taskType.equals("SQL_TASK") || taskType.equals("MANUAL_TASK"))) {
            throw new RuntimeException("Wrong request for task type");
        }

        if((task.getDeadLine().getTime() - new Date().getTime() < 0)) {
            throw new RuntimeException("The deadline expired");
        }
        Comparator<Attempts> comparator = (p1, p2) -> (int) (p2.getTime().getTime() - p1.getTime().getTime());
        attempts.sort(comparator);


        if(attempts.size() > 0 && (new Date().getTime() - attempts.get(0).getTime().getTime() < attempts.size() *60*1000)) {
            resultsResponse.setTimeout((int) (attempts.size()*60*1000 - (new Date().getTime() - attempts.get(0).getTime().getTime())));
            return resultsResponse;
        }

        // SQL_TASK - attempts restricted by time, but results are shown immediately
        // MANUAL_TASK - attempts restricted by time
        if(taskType.equals("SQL_TASK")) {

            try {
                List<List<String>> studentResults = runSQLQueryUser(solution, "opentests");
                resultsResponse.setOpenResult(studentResults);

            } catch (Exception e) {
                log.error(e.getMessage());
            }

            resultsResponse.setTimeout((attempts.size() + 1)*60*1000);
        } else {
            resultsResponse.setTimeout((attempts.size() + 1)*60*1000);
        }

        Attempts attempt = new Attempts(userId, taskId, false, solution);
        attemptsRepo.save(attempt);

        return resultsResponse;
    }

    @Override
    public ResultScoreResponse checkSQLSolutionScore(Integer taskId, String solution) {
        ResultScoreResponse resultsResponse = new ResultScoreResponse();
        Integer userId = appUserRepo.findByLogin(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()).getId();
        Tasks task = tasksRepo.findById(taskId).get();
        List<Scores> scores = scoresRepo.findAllByUserIdAndTaskId(taskId,userId);
        String taskType = taskTypesRepo.getById(task.getTaskTypeId()).getName();

        if(!(taskType.equals("SQL_TASK") || taskType.equals("MANUAL_TASK"))) {
            throw new RuntimeException("Wrong request for task type");
        }

        if((task.getDeadLine().getTime() - new Date().getTime() < 0)) {
            throw new RuntimeException("The deadline expired");
        }
        Comparator<Scores> comparator = (p1, p2) -> (int) (p2.getDate().getTime() - p1.getDate().getTime());
        scores.sort(comparator);


        if(scores.size() > 0 && (new Date().getTime() - scores.get(0).getDate().getTime() < scores.size() *60*1000)) {
            resultsResponse.setTimeout((int) (scores.size()*60*1000 - (new Date().getTime() - scores.get(0).getDate().getTime())));
            return resultsResponse;
        }

        //needed logic go check solution
        boolean succeeded = false;

        // SQL_TASK - attempts restricted by time, but results are shown immediately
        // MANUAL_TASK - attempts restricted by time
        if(taskType.equals("SQL_TASK")) {
            boolean noErrors = true;
            boolean noOpenTestsError = true;
            boolean noHiddenTestError = true;

            try {

                List<List<String>> studentResults = runSQLQueryUser(solution, "opentests");
                List<List<String>> teacherResults = runSQLQueryUser(tasksRepo.findById(taskId).get().getSolution(), "opentests");

                if(studentResults.size() != teacherResults.size()) {
                    noOpenTestsError = false;
                }

                if(!studentResults.equals(teacherResults)) {
                    noOpenTestsError = false;
                }
                Result result = new Result("Open test",noOpenTestsError);
                resultsResponse.getResults().add(result);

            } catch (Exception e) {
                log.error(e.getMessage());
                noOpenTestsError = false;
                Result result = new Result("Open test",false);
                resultsResponse.getResults().add(result);
            }

            try {

                List<List<String>> studentResults = runSQLQueryUser(solution, "hiddenTests");
                List<List<String>> teacherResults = runSQLQueryUser(tasksRepo.findById(taskId).get().getSolution(), "hiddenTests");

                if(studentResults.size() != teacherResults.size()) {
                    noHiddenTestError = false;
                }

                if(!studentResults.equals(teacherResults)) {
                    noHiddenTestError = false;
                }

                /*for(int i = 0; i < studentResults.size(); ++i) {
                    if(studentResults.get(i).size() != teacherResults.get(i).size()) {
                        noHiddenTestError = false;
                    }
                    for (int k = 0; k < studentResults.get(i).size(); ++k){
                        if(!studentResults.get(i).get(k).equals(teacherResults.get(i).get(k))) {
                            noHiddenTestError = false;
                        }
                    }
                }*/
                Result result = new Result("Hidden test",noHiddenTestError);
                resultsResponse.getResults().add(result);

            } catch (Exception e) {
                log.error(e.getMessage());
                noHiddenTestError = false;
                Result result = new Result("Hidden test",false);
                resultsResponse.getResults().add(result);
            }

            resultsResponse.setTimeout((scores.size() + 1)*60*1000);

            Integer score = 1;
            if(noHiddenTestError && noOpenTestsError) {
                score = 5;
            }
            scoresRepo.save(new Scores(userId, taskId, score, 1,solution));

        } else {
            resultsResponse.setTimeout((scores.size() + 1)*60*1000);
        }
        return resultsResponse;
    }

    @Override
    public Integer checkSimpleSolution(Integer taskId, List<Integer> solutionsId) {
        Integer userId = appUserRepo.findByLogin(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()).getId();
        Tasks task = tasksRepo.findById(taskId).get();
        List<Attempts> attempts = attemptsRepo.findAllByTaskIdAndUserId(taskId,userId);
        String taskType = taskTypesRepo.getById(task.getTaskTypeId()).getName();
        if(!taskType.equals("SIMPLE_TASK")) {
            throw new RuntimeException("Wrong request for task type");
        }
        if(attempts.size() > 0) {
            throw new RuntimeException("Only 1 attempt allowed");
        }
        if(task.getDeadLine().getTime() - new Date().getTime() < 0) {
            throw new RuntimeException("The deadline expired");
        }


        // SIMPLE_TASK - only 1 attempt
        List<SolutionVariants> solutionVariants = solutionVariantsRepo.findAllByTaskId(taskId);
        List<Integer> rightSolutions = new ArrayList<>();

        for (SolutionVariants solutionVariant: solutionVariants) {
            if(solutionVariant.getIsAnswer()) {
                rightSolutions.add(solutionVariant.getId());
            }
        }
        if(rightSolutions.size() == 0) {
            throw new RuntimeException("Task doesn't have right solutions, contact administrator");
        }
        Integer numberOfWrongSolutions = 0;
        Integer numberOfRightSolutions = 0;

        for(Integer solutionId: solutionsId) {
            if(!rightSolutions.contains(solutionId)) {
                numberOfWrongSolutions++;
            } else {
                numberOfRightSolutions++;
            }
        }
        Integer result = numberOfRightSolutions/rightSolutions.size()*4 - numberOfWrongSolutions*2 + 1;
        if(result < 1) {
            result = 1;
        }
        Attempts attempt = new Attempts(userId, taskId, result == 5? true : false, solutionsId.toString());
        attemptsRepo.save(attempt);
        Scores score = new Scores(userId, taskId, result, 1, solutionsId.toString());
        scoresRepo.save(score);
        return result;
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



