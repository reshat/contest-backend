package com.group.contestback.responseTypes;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResultsResponse {
    private Integer timeout;//seconds
    //needed class to represent result from open table
    //needed class to represent if solution worked on open table
    private List<Result> results = new ArrayList<>();
}