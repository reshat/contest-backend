package com.group.contestback.services;

import com.group.contestback.models.*;
import com.group.contestback.repositories.*;
import com.group.contestback.responseTypes.GroupCoursesWithNames;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TaskServiceClass implements TaskService{
    private final TasksRepo tasksRepo;
    private final TaskTypesRepo taskTypesRepo;
    private final AppUserService appUserService;
    private final CoursesRepo coursesRepo;
    private final TaskCoursesRepo taskCoursesRepo;
    private final GroupsRepo groupsRepo;
    private final GroupCoursesRepo groupCoursesRepo;
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

    @Override
    public String addTaskToCourse(Integer taskId, Integer courseId) {
        Optional<Tasks> task = tasksRepo.findById(taskId);
        Optional<Courses> course = coursesRepo.findById(courseId);
        if(task.isEmpty()) {
            log.error("there is no task with this id");
        } else if(course.isEmpty()) {
            log.error("there is no course with this id");
        } else {
            TaskCourses taskCourses = new TaskCourses(taskId, courseId);
            taskCoursesRepo.save(taskCourses);
        }
        return "";
    }

    @Override
    public List<Groups> getAllGroups() {
        return groupsRepo.findAll();
    }

    @Override
    public void addGroup(String number, Integer year) {
        Groups groups = new Groups(number, year);
        groupsRepo.save(groups);
    }

    @Override
    public List<GroupCoursesWithNames> getAllGroupCourses() {
        List<GroupCoursesWithNames> groupCoursesWithNames = new ArrayList<>();
        groupCoursesRepo.findAll().forEach(groupCourses -> groupCoursesWithNames.add(new GroupCoursesWithNames(
                groupCourses.getId()
                ,coursesRepo.getById(groupCourses.getCourseId()).getName(),
                groupCourses.getCourseId(),
                groupsRepo.getById(groupCourses.getGroupId()).getNumber(),
                groupCourses.getGroupId()
        ))
        );
        return groupCoursesWithNames;
    }

    @Override
    public String addGroupOnCourse(Integer courseId, Integer groupId) {
        Optional<Courses> courses = coursesRepo.findById(courseId);
        Optional<Groups> groups = groupsRepo.findById(groupId);
        if(courses.isEmpty()) {
            log.error("there is no course with this id");
            return "there is no course with this id";
        } else if(groups.isEmpty()) {
            log.error("there is no group with this id");
            return "there is no group with this id";
        } else {
            GroupCourses groupCourses = new GroupCourses(courseId, groupId);
            groupCoursesRepo.save(groupCourses);
        }
        return "";
    }
}
//    private Integer id;
//    private String courseName;
//    private Integer courseId;
//    private String groupName;
//    private Integer groupId;
