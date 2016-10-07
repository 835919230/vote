package com.hc.service.impl;

import com.hc.dao.ChoiceDao;
import com.hc.dao.UserDao;
import com.hc.dao.VoteDao;
import com.hc.model.Choice;
import com.hc.model.User;
import com.hc.model.Vote;
import com.hc.service.VoteService;
import com.hc.util.TimeUtils;
import com.hc.web.controller.VoteController;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by hexi on 16-10-5.
 */
@Service("voteService")
public class VoteServiceImpl implements VoteService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("voteDao")
    private VoteDao voteDao;

    @Autowired
    @Qualifier("choiceDao")
    private ChoiceDao choiceDao;

    @Autowired
    @Qualifier("userDao")
    private UserDao userDao;

    @Override
    public List<Vote> listAll() {
        return voteDao.findAll();
    }

    @Override
    public Vote findVoteById(long id) {
        return voteDao.findOne(id);
    }

    @Transactional
    @Override
    public Vote updateChoiceNumber(Long[] choiceIds) {
        if (ArrayUtils.isNotEmpty(choiceIds)) {
            List<Choice> choicesToUpdate = new ArrayList<>();
            for (Long choiceId : choiceIds) {
                if (choiceId == null || choiceId <= 0L)
                    return null;
                Choice one = choiceDao.findOne(choiceId);
                if (!TimeUtils.isNotExpired(one.getVote()))
                    return null;
                if (one != null) {
                    one.increNumber();
                    choicesToUpdate.add(one);
                }
            }
            choiceDao.save(choicesToUpdate);
            Vote vote = choicesToUpdate.get(0).getVote();
            vote.increParNumber();
            voteDao.save(vote);
            return vote;
        }
        return null;
    }

    @Transactional
    @Override
    public Vote addAVote(HttpServletRequest request) {
        String voteTitle = request.getParameter("voteTitle");
        String voteDesc = request.getParameter("voteDesc");
        String voteEndDate = request.getParameter("voteEndDate");
        logger.info("获取到的Date:{}"+voteEndDate);
        String[] choiceNames = request.getParameterValues("choice");
        String isMultipleString = request.getParameter("isMultiple");
        Vote vote = null;
        if (StringUtils.isNoneEmpty(voteTitle,voteDesc,voteEndDate) && choiceNames != null) {
            boolean isMultiple = isMultipleString != null;
            vote = new Vote(voteTitle, voteDesc, 0, isMultiple, new Date(), new Date(), TimeUtils.afterDay(7L));
            // TODO: 16-10-6 暂时规定7天，看情况改
            HttpSession session = request.getSession();
            Object attribute = session.getAttribute(VoteController.SPRING_SECURITY_CONTEXT);
            SecurityContext context = (SecurityContext) attribute;
            String username = (String) context.getAuthentication().getPrincipal();
            User user = userDao.findByUsername(username);
            vote.setUser(user);
            List<Choice> choices = new ArrayList<>();
            for (String choiceName : choiceNames) {
                Choice choice = new Choice(choiceName, 0, vote);
                logger.info("chooiceName:{}",choiceName);
                choices.add(choice);
            }
            vote.setChoices(choices);
            Vote save = voteDao.save(vote);
            return save;
        }
        return vote;
    }

}
