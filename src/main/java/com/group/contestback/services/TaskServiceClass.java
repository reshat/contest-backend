package com.group.contestback.services;

import com.group.contestback.models.AppUser;
import com.group.contestback.models.Courses;
import com.group.contestback.models.TaskTypes;
import com.group.contestback.models.Tasks;
import com.group.contestback.repositories.CoursesRepo;
import com.group.contestback.repositories.TaskTypesRepo;
import com.group.contestback.repositories.TasksRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TaskServiceClass implements TaskService{
    private final TasksRepo tasksRepo;
    private final TaskTypesRepo taskTypesRepo;
    private final AppUserService appUserService;
    private final CoursesRepo coursesRepo;
    @Override
    public void addTaskType(String name) {
        TaskTypes taskTypes = new TaskTypes(name);
        taskTypesRepo.save(taskTypes);
    }

    @Override
    public List<TaskTypes> getTaskTypes() {
        return taskTypesRepo.findAll();
    }

    @Override
    public void addTask(String name, String solution, String deadline, String description, Integer taskTypeId){
        try {
            Tasks tasks = new Tasks(name, solution, deadline, description, taskTypeId);
            tasksRepo.save(tasks);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public List<Tasks> getTasks() {
        return tasksRepo.findAll();
    }

    @Override
    public List<Tasks> getTasksByUser(String login) {
        AppUser user = appUserService.getAppUser(login);
        return tasksRepo.findAll(); // temporary solution
    }

    @Override
    public List<Courses> getAllCourses() {
        return coursesRepo.findAll();
    }

    @Override
    public void addCourse(String name, Integer year) {
        Courses courses = new Courses(name, year);
        coursesRepo.save(courses);
    }
}
