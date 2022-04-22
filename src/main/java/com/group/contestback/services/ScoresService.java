package com.group.contestback.services;

import com.group.contestback.models.Attempts;
import com.group.contestback.models.Scores;
import com.group.contestback.responseTypes.GroupCoursesScoresResponse;
import com.group.contestback.responseTypes.GroupStudents;
import com.group.contestback.responseTypes.ResultScoreResponse;
import com.group.contestback.responseTypes.ResultsResponse;

import java.text.ParseException;
import java.util.List;

public interface ScoresService {
    void addScore(Scores score);
    List<Scores> getAllScores();
    void addAttempt(Attempts attempt);
    List<Attempts> getAllAttempts();
    ResultsResponse checkSQLSolution(Integer taskId, Integer courseId, String solution) throws ParseException;
    ResultScoreResponse checkSQLSolutionScore(Integer taskId, Integer courseId, String solution) throws ParseException;
    Integer checkSimpleSolution(Integer taskId, Integer courseId, List<Integer> solutionsId);
    List<Scores> getStudentScores();
    List<Scores> getGroupScoresForTask(Integer groupId, Integer taskId);
    List<Attempts> getStudentAttemptsOnTask(Integer taskId);
    GroupCoursesScoresResponse getGroupScoresForCourse(Integer groupId, Integer taskId);
    List<GroupStudents> getAllManualAttempts(String courseId);
}
