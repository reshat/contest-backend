package com.group.contestback.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tasks {
    @Id
    private Integer id;
    private String name;
    private String description;
    private String solution;
    private Date deadLine;
    private Integer taskTypeId;
}
