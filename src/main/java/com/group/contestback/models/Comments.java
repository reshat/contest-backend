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
public class Comments {
    @Id
    private Integer id;
    @Column(name="totaskid")
    private Integer toTaskId;
    @Column(name="fromuserid")
    private Integer fromUserId;
    private Date date;
    private Boolean deleted;
}
