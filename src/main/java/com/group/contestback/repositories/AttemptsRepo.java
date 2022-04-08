package com.group.contestback.repositories;

import com.group.contestback.models.Attempts;
import com.group.contestback.models.Scores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttemptsRepo extends JpaRepository<Attempts, Integer> {
    List<Attempts> findAllByTaskIdAndUserId(Integer taskId, Integer userId);
    @Query(
            value = "select a.id, a.userid, a.taskid, a.time, a.succeeded, a.solution" +
                    " from (select Max(time) as maxTime, taskid " +
                    "from attempts where userid = ?1 group by taskid)" +
                    " r inner join attempts a on r.taskid = a.taskid and a.time = r.maxTime",
            nativeQuery = true)
    List<Attempts> findByTaskUserMaxTime(Integer userId);
}
