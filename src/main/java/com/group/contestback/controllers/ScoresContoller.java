package com.group.contestback.controllers;

import com.group.contestback.models.Attempts;
import com.group.contestback.models.Scores;
import com.group.contestback.security.CustomAccessDeniedHandler;
import com.group.contestback.services.AppUserService;
import com.group.contestback.services.ScoresService;
import com.group.contestback.services.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.Date;
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
        try {
            Scores score = new Scores(scoreForm.getUserId(),scoreForm.getTaskId(),scoreForm.getScore(),scoreForm.getTeacherId(),scoreForm.getSolution());
            scoresService.addScore(score);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @ApiOperation(value = "Возращает все оценки")
    @GetMapping("/allScores")
    public ResponseEntity<?> getAllScores() {
        return ResponseEntity.ok().body(scoresService.getAllScores());
    }

    @ApiOperation(value = "Добавляет новую попытку")
    @PostMapping("/addAttempt")
    public ResponseEntity<?> addAttempt(@RequestBody AttemptForm attemptForm) {
        try {
            // Check logic needed
            // depending on taskTypeId
            return ResponseEntity.ok().body(scoresService.checkSolution(attemptForm.getUserId(),attemptForm.getTaskId(),attemptForm.getSolution()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
    private String solution;
}
@Data
class AttemptForm {
    private Integer userId;
    private Integer taskId;
    private String solution;
}
