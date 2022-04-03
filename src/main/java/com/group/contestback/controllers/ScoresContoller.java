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

import javax.persistence.Column;
import java.util.Date;

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

    @ApiOperation(value = "Добавляет новый тип заданий")
    @PostMapping("/addScore")
    public ResponseEntity<?> addScore(@RequestBody ScoreForm scoreForm) {
        try {
            Scores score = new Scores(scoreForm.getUserId(),scoreForm.getTaskId(),scoreForm.getScore(),scoreForm.getDate(),scoreForm.getTeacherId(),scoreForm.getSolution());
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
}
@Data
class ScoreForm {
    private Integer userId;
    private Integer taskId;
    private Integer score;
    private String date;
    private Integer teacherId;
    private String solution;
}