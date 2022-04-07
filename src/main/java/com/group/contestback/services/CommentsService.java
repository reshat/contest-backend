package com.group.contestback.services;

import com.group.contestback.models.Comments;

import java.util.List;

public interface CommentsService {
    void addComment(Comments comment);
    List<Comments> getAllComments();
    List<Comments> getCommentsToTask(Integer toTaskId);
}
