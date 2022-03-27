package com.group.contestback.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "taskcourses")
public class TaskCourses {
    @Id
    private Integer id;
    @Column(name="taskid")
    private Integer taskId;
    @Column(name="courseid")
    private Integer courseId;
}
