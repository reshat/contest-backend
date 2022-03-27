package com.group.contestback.services;

import com.group.contestback.models.TaskTypes;
import com.group.contestback.models.Tasks;

import java.util.List;

public interface TaskService {
    void addTaskType(String name);
    List<TaskTypes> getTaskTypes();
    void addTask(String name, String solution, String deadline, String description, Integer taskTypeId);
    List<Tasks> getTasks();
    List<Tasks> getTasksByUser(String login);

}
