package com.hc;

import com.hc.dao.UserDao;
import com.hc.dao.VoteDao;
import com.hc.model.Choice;
import com.hc.model.User;
import com.hc.model.Vote;
import com.hc.util.TimeUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VoteApplicationTests {

    static Logger logger = LoggerFactory.getLogger(VoteApplicationTests.class);

	@Autowired
	VoteDao voteDao;

    @Autowired
    UserDao userDao;

	@Test
	public void contextLoads() {
	}

    @Test
    public void userDao() {
//        User user = new User();
//        user.setId(1001L);
//        user.setUsername("2014010908013");
//        user.setPassword("123456");
//        user.setNickname("HeXi");
//        user.setCreateTime(new Date());
//        user.setLastLoginIp("192.168.1.103");
//        user.setLastLoginTime(new Date());
//        userDao.save(user);
            Integer i= userDao.updateNicknameById(1001L, "何肸12");
            logger.debug(i.toString());
    }

    @Test
    public void queryUser() {
        User one = userDao.findOne(1001L);
        Set<Vote> votes = one.getVotes();
        if (votes != null) {
            for (Vote vote : votes)
                System.out.println(vote);
        }
    }

    @Test
    public void deleteUser() {
        userDao.delete(1001L);
    }

    @Test
    public void voteInsert() {
        Vote vote = new Vote();
        vote.setId(103L);
        vote.setTitle("test");
        vote.setDescription("test_desc");
        vote.setParticipateNumber(0);
        Date now = new Date();
        vote.setCreateTime(now);
        vote.setEndTime(TimeUtils.afterDay(3L));
        vote.setStartTime(now);
        vote.setUser(userDao.findOne(1001L));
        Vote save = voteDao.save(vote);
        Assert.assertTrue(save != null);
        System.out.println(save);
    }

    @Test
    public void voteDelete() {
        voteDao.delete(103L);
    }

    @Test
    public void voteQuery() {
        List<Vote> all = voteDao.findAll();
        if (all != null) {
            for (Vote vote : all)
                System.out.println(vote);
        }
    }

    @Test
    public void voteQue() {
        Vote one = voteDao.findOne(103L);
        if (one != null) {
            System.out.println(one);
        }
    }

    @Test
    public void voteUpdate() {
        Vote one = voteDao.findOne(103L);
        one.setTitle("update_title_test");
        voteDao.save(one);
        System.out.println(one);
    }

    @Test
    public void voteGet() {
        Vote one = voteDao.findOne(101L);
        Set<Choice> choices = one.getChoices();

        if (choices != null) {
            System.out.println(choices.size());
            for (Choice c : choices) {
                System.out.println(c.toString());
            }
        }
    }

    @Test
    public void findByUsername() {
        User user = userDao.findByUsername("2014010908013");
        System.out.println(user.getNickname());
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        for (GrantedAuthority authority:authorities) {
            System.out.println(authority.getAuthority());
        }
    }

}
