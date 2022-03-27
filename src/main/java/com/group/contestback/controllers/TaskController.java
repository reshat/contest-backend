package com.group.contestback.controllers;

import com.group.contestback.models.AppUser;
import com.group.contestback.services.AppUserService;
import com.group.contestback.services.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
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
    public ResponseEntity<?> addTaskType(@RequestBody String name) {
        taskService.addTaskType(name);
        return ResponseEntity.ok().build();
    }
    @ApiOperation(value = "Возращает все типы заданий")
    @GetMapping("/taskTypes")
    public ResponseEntity<?> getTaskTypes() {
        return ResponseEntity.ok().body(taskService.getTaskTypes());
    }
    @ApiOperation(value = "Добавляет новое задание")
    @PostMapping("/addtask")
    public ResponseEntity<?> addTask(@RequestBody addTaskForm form) {
        taskService.addTask(form.getName(), form.getSolution(), form.getDeadline(), form.getDescription(), form.getTaskTypeId());
        return ResponseEntity.ok().build();
    }
    @ApiOperation(value = "Возращает все задания")
    @GetMapping("/allTasks")
    public ResponseEntity<?> getAllTasks() {
        return ResponseEntity.ok().body(taskService.getTasks());
    }
    @ApiOperation(value = "Возращает все курсы")
    @GetMapping("/allCourses")
    public ResponseEntity<?> getAllCourses() {
        return ResponseEntity.ok().body(taskService.getAllCourses());
    }
    @ApiOperation(value = "Добавляет новый курс")
    @PostMapping("/addCourse")
    public ResponseEntity<?> addCourse(@RequestBody addCourseForm form) {
        taskService.addCourse(form.getName(), form.getYear());
        return ResponseEntity.ok().build();
    }
    @ApiOperation(value = "Добавляет задание на курс")
    @PostMapping("/addTaskToCourse")
    public ResponseEntity<?> addTaskToCourse(@RequestBody addTaskToCourseForm form) {
        return ResponseEntity.ok().body(taskService.addTaskToCourse(form.getTaskId(), form.getCourseId()));
    }
    @ApiOperation(value = "Возращает все группы")
    @GetMapping("/allGroups")
    public ResponseEntity<?> getAllGroups() {
        return ResponseEntity.ok().body(taskService.getAllGroups());
    }
    @ApiOperation(value = "Добавляет новую группу")
    @PostMapping("/addGroup")
    public ResponseEntity<?> addGroup(@RequestBody addGroupForm form) {
        taskService.addGroup(form.getNumber(), form.getYear());
        return ResponseEntity.ok().build();
    }
    @ApiOperation(value = "Возращает таблицу со всеми группами и их курсами")
    @GetMapping("/allGroupsCourses")
    public ResponseEntity<?> getAllGroupsCourses() {
        return ResponseEntity.ok().body(taskService.getAllGroupCourses());
    }
    @ApiOperation(value = "Добавляет группу на курс")
    @PostMapping("/addGroupOnCourse")
    public ResponseEntity<?> addGroupOnCourse(@RequestBody addGroupOnCourse form) {
        return ResponseEntity.ok().body(taskService.addGroupOnCourse(form.getCourseId(), form.getGroupId()));
    }
}
@Data
class addTaskForm {
    private String name;
    private String solution;
    private String deadline;
    private String description;
    private Integer taskTypeId;
}
@Data
class addCourseForm {
    private String name;
    private Integer year;
}
@Data
class addTaskToCourseForm {
    private Integer taskId;
    private Integer courseId;
}
@Data
class addGroupForm {
    private String number;
    private Integer year;
}
@Data
class addGroupOnCourse {
    private Integer courseId;
    private Integer groupId;
}