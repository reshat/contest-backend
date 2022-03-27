package com.group.contestback.controllers;

import com.group.contestback.models.AppUser;
import com.group.contestback.services.AppUserService;
import com.group.contestback.services.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"Task controller"})
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
@RequestMapping("/task")
public class TaskController {
    private final AppUserService userService;
    private final TaskService taskService;

    @ApiOperation(value = "Добавляет новый тип заданий")
    @PostMapping("/addtasktype")
    public ResponseEntity<List<AppUser>> addTaskType(@RequestBody String name) {
        taskService.addTaskType(name);
        return ResponseEntity.ok().build();
    }
}
