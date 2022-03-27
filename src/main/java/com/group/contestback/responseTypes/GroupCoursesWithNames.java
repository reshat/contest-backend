package com.group.contestback.responseTypes;
import com.group.contestback.models.Courses;
import com.group.contestback.models.Groups;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class GroupCoursesWithNames {
    private Integer id;
    private String courseName;
    private Integer courseId;
    private String groupName;
    private Integer groupId;

    public GroupCoursesWithNames(Integer id, String byId, Integer courseId, String byId1, Integer groupId) {
        this.id = id;
        this.courseName = byId;
        this.courseId = courseId;
        this.groupName = byId1;
        this.groupId = groupId;
    }
}
