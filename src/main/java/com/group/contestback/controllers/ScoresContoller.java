package com.group.contestback.controllers;

import com.group.contestback.models.Scores;
import com.group.contestback.services.AppUserService;
import com.group.contestback.services.ScoresService;
import com.group.contestback.services.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@Api(tags = {"Scores controller"})
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
@RequestMapping("/score")
public class ScoresContoller {
    private final AppUserService userService;
    private final TaskService taskService;
    private final ScoresService scoresService;

    @ApiOperation(value = "Добавляет новую оценку")
    @PostMapping("/addScore")
    public ResponseEntity<?> addScore(@RequestBody ScoreForm scoreForm) {
        Scores score = new Scores(scoreForm.getUserId(),scoreForm.getTaskId(),
                    scoreForm.getScore(),scoreForm.getTeacherId(),scoreForm.getReview(), scoreForm.getCourseId());
        scoresService.addScore(score);
        return ResponseEntity.ok().build();
    }
    @ApiOperation(value = "Возращает все оценки")
    @GetMapping("/allScores")
    public ResponseEntity<?> getAllScores() {
        return ResponseEntity.ok().body(scoresService.getAllScores());
    }

    @ApiOperation(value = "Возращает оценки студента")
    @GetMapping("/studentScores")
    public ResponseEntity<?> getStudentScores() {
        return ResponseEntity.ok().body(scoresService.getStudentScores());
    }

    @ApiOperation(value = "Возращает попытки студента к заданию")
    @GetMapping("/studentAttempts/{taskId}")
    public ResponseEntity<?> getStudentAttempts(@PathVariable String taskId) {
        return ResponseEntity.ok().body(scoresService.getStudentAttemptsOnTask(Integer.parseInt(taskId)));
    }

    @ApiOperation(value = "Возращает по группам непроверяемые попытки")
    @GetMapping("/allManualAttempts/{courseId}")
    public ResponseEntity<?> getAllManualAttempts(@PathVariable String courseId) {
        return ResponseEntity.ok().body(scoresService.getAllManualAttempts(courseId));
    }


    @ApiOperation(value = "Возращает оценки группы по заданию")
    @GetMapping("/groupScoresForTask")
    public ResponseEntity<?> getGroupScoresForTask(@RequestBody GroupTask groupTask) {
        return ResponseEntity.ok().body(scoresService.getGroupScoresForTask(groupTask.getGroupId(), groupTask.getTaskId()));
    }

    @ApiOperation(value = "Возращает оценки группы по курсу")
    @GetMapping("/groupScoresForCourse")
    public ResponseEntity<?> getGroupScoresForTask(@RequestBody GroupCourse groupCourse) {
        return ResponseEntity.ok().body(scoresService.getGroupScoresForCourse(groupCourse.getGroupId(), groupCourse.getCourseId()));
    }

    @ApiOperation(value = "Добавляет новую попытку на оценку")
    @PostMapping("/addSQLAttemptScore")
    public ResponseEntity<?> addSQLAttemptScore(@RequestBody AttemptSQLForm attemptForm) throws ParseException {
        return ResponseEntity.ok().body(scoresService.checkSQLSolutionScore(attemptForm.getTaskId(), attemptForm.getCourseId(),attemptForm.getSolution()));
    }
    @ApiOperation(value = "Добавляет новую попытку")
    @PostMapping("/addSQLAttempt")
    public ResponseEntity<?> addSQLAttempt(@RequestBody AttemptSQLForm attemptForm) throws ParseException {
        return ResponseEntity.ok().body(scoresService.checkSQLSolution(attemptForm.getTaskId(), attemptForm.getCourseId(),attemptForm.getSolution()));
    }
    @ApiOperation(value = "Добавляет новую попытку")
    @PostMapping("/addSimpleAttempt")
    public ResponseEntity<?> addSimpleAttempt(@RequestBody AttemptSimpleForm attemptForm) {
        return ResponseEntity.ok().body(scoresService.checkSimpleSolution(attemptForm.getTaskId(), attemptForm.getCourseId(),attemptForm.getSolutionsId()));
    }

    @ApiOperation(value = "Возращает все попытки")
    @GetMapping("/allAttempts")
    public ResponseEntity<?> getAllAttempts() {
        return ResponseEntity.ok().body(scoresService.getAllAttempts());
    }
}
@Data
class ScoreForm {
    private Integer userId;
    private Integer taskId;
    private Integer score;
    private Integer teacherId;
    private String review;
    private Integer courseId;
}
@Data
class AttemptSQLForm {
    private Integer taskId;
    private Integer courseId;
    private String solution;
}
@Data
class AttemptSimpleForm {
    private Integer taskId;
    private Integer courseId;
    private List<Integer> solutionsId;
}
@Data
class GroupTask {
    private Integer groupId;
    private Integer taskId;
}
@Data
class GroupCourse {
    private Integer courseId;
    private Integer groupId;
}
