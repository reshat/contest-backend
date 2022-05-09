package com.group.contestback.responseTypes;

import com.group.contestback.models.Scores;
import lombok.Data;

import java.util.List;

@Data
public class ScoresUser {
    UserPageResponse user;
    Scores scores;
}
