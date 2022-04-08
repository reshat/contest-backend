package com.group.contestback.controllers;

import com.group.contestback.models.Comments;
import com.group.contestback.models.Scores;
import com.group.contestback.repositories.CommentsRepo;
import com.group.contestback.services.CommentsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Column;
import java.util.Date;

@Api(tags = {"Comments controller"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class CommentsController {
    private final CommentsService commentsService;
    @ApiOperation(value = "Добавляет новый комментарий")
    @PostMapping("/addComment")
    public ResponseEntity<?> addScore(@RequestBody CommentsForm commentsForm) {
        try {
            Comments comment = new Comments(commentsForm.getToTaskId(), commentsForm.getFromUserId(), commentsForm.getSolution());
            log.info(comment.toString());
            commentsService.addComment(comment);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @ApiOperation(value = "Возращает все коментарии")
    @GetMapping("/allComments")
    public ResponseEntity<?> getAllComments() {
        return ResponseEntity.ok().body(commentsService.getAllComments());
    }
    @ApiOperation(value = "Возращает все коментарии к заданию")
    @GetMapping("/getCommentsToTask")
    public ResponseEntity<?> getAllCommentsToTask(@RequestBody Integer toTaskId) {
        return ResponseEntity.ok().body(commentsService.getCommentsToTask(toTaskId));
    }
    @ApiOperation(value = "Удаление комментария")
    @PostMapping("/removeComment")
    public ResponseEntity<?> removeComment(@RequestBody String commentId) {
        commentsService.removeComment(Integer.parseInt(commentId));
        return ResponseEntity.ok().build();
    }
}
@Data
class CommentsForm {
    private Integer toTaskId;
    private Integer fromUserId;
    private String solution;
}