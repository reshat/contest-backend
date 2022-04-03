package com.group.contestback.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Scores {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name="userid")
    private Integer userId;
    @Column(name="taskid")
    private Integer taskId;
    private Integer score;
    private Date date;
    @Column(name="teacherid")
    private Integer teacherId;
    private String solution;

    public Scores(Integer userId, Integer taskId, Integer score, String date, Integer teacherId, String solution) throws ParseException {
        this.userId = userId;
        this.taskId = taskId;
        this.score = score;
        SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        this.date = formatter.parse(date);
        this.teacherId = teacherId;
        this.solution = solution;
    }
}
