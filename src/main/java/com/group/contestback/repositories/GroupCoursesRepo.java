package com.group.contestback.repositories;

import com.group.contestback.models.GroupCourses;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupCoursesRepo extends JpaRepository<GroupCourses, Integer> {
}
