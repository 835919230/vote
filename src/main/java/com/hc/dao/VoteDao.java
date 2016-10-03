package com.hc.dao;

import com.hc.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * Created by hexi on 16-10-3.
 */
@Component("voteDao")
public interface VoteDao extends JpaRepository<Vote, Long> {
}
