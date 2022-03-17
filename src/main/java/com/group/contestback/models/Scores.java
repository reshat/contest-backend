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
public class Scores {
    @Id
    private Integer id;
    private Integer userId;
    private Integer taskId;
    private Integer score;
    private Date date;
    private Integer teacherId;
}
