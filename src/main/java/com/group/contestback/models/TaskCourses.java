package com.group.contestback.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskCourses {
    @Id
    private Integer id;
    private Integer taskId;
    private Integer courseId;
}
