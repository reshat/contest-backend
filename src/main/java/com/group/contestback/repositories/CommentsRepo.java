package com.group.contestback.repositories;

import com.group.contestback.models.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepo extends JpaRepository<Comments, Integer> {
}
