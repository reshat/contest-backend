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
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String description;
    private String solution;
    private Date deadLine;
    @Column(name="tasktypeid")
    private Integer taskTypeId;

    public Tasks(String name, String solution, String deadline, String description, Integer taskTypeId) throws ParseException {
        this.name = name;
        this.solution = solution;
        //"30-03-2022 23:37:50";

        SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        this.deadLine = formatter.parse(deadline);
        this.description = description;
        this.taskTypeId = taskTypeId;
    }
}
