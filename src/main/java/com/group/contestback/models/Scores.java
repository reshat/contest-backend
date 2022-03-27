package com.group.contestback.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Scores {
    @Id
    private Integer id;
    @Column(name="userid")
    private Integer userId;
    @Column(name="taskid")
    private Integer taskId;
    private Integer score;
    private Date date;
    @Column(name="teacherid")
    private Integer teacherId;
}