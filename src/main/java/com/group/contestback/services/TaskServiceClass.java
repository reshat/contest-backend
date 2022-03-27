package com.group.contestback.services;

import com.group.contestback.models.TaskTypes;
import com.group.contestback.repositories.TaskTypesRepo;
import com.group.contestback.repositories.TasksRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TaskServiceClass implements TaskService{
    private final TasksRepo tasksRepo;
    private final TaskTypesRepo taskTypesRepo;
    @Override
    public void addTaskType(String name) {
        TaskTypes taskTypes = new TaskTypes(name);
        taskTypesRepo.save(taskTypes);
    }
}
