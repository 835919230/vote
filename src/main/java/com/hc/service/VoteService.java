package com.hc.service;

import com.hc.model.Choice;
import com.hc.model.Vote;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by hexi on 16-10-3.
 */
public interface VoteService {
    List<Vote> listAll();
    Vote findVoteById(long id);
    Vote updateChoiceNumber(Long[] choiceIds);

    Vote addAVote(HttpServletRequest request);
}
