package com.hc.dao;

import com.hc.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by hexi on 16-10-3.
 */
@Component("voteDao")
@Transactional
public interface VoteDao extends JpaRepository<Vote, Long> {
}
