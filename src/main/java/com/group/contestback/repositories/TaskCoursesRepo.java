package com.group.contestback.repositories;

import com.group.contestback.models.TaskCourses;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskCoursesRepo  extends JpaRepository<TaskCourses, Integer> {
}
