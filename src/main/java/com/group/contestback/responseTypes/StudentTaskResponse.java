package com.group.contestback.responseTypes;

import com.group.contestback.models.Courses;
import com.group.contestback.models.Tasks;
import lombok.Data;

import java.util.List;

@Data
public class StudentTaskResponse {
    private Integer userId;
    private List<Courses> courses;
}
