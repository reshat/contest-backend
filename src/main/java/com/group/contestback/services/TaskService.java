package com.group.contestback.services;

import com.group.contestback.models.Courses;
import com.group.contestback.models.Groups;
import com.group.contestback.models.TaskTypes;
import com.group.contestback.models.Tasks;
import com.group.contestback.responseTypes.GroupCoursesWithNames;
import com.group.contestback.responseTypes.StudentTaskResponse;
import com.group.contestback.responseTypes.TaskResponse;

import java.util.List;

public interface TaskService {
    void addTaskType(String name);
    List<TaskTypes> getTaskTypes();
    void addTask(String name, String solution, String description, Integer taskTypeId);
    List<TaskResponse> getTasks();
    List<TaskResponse> getTasksByCourse(Integer courseId);
    List<Courses> getAllCourses();
    void addCourse(String name, Integer year);
    String addTaskToCourse(Integer taskId, Integer courseId);
    List<Groups> getAllGroups();
    void addGroup(String number, Integer year);
    List<GroupCoursesWithNames> getAllGroupCourses();
    String addGroupOnCourse(Integer courseId, Integer groupId);
    StudentTaskResponse getStudentCourses();
}
