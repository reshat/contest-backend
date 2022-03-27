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
@Table(name = "groupcourses")
public class GroupCourses {
    @Id
    private Integer id;
    @Column(name="courseid")
    private Integer courseId;
    @Column(name="groupid")
    private Integer groupId;
}
