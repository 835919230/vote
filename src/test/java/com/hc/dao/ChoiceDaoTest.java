package com.hc.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by hexi on 16-10-3.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ChoiceDaoTest {

    @Autowired
    ChoiceDao choiceDao;

    @Test
    public void incrementNumber() throws Exception {
        int effectNumber = choiceDao.incrementNumber("XXX1");
        Assert.assertEquals(effectNumber,1);
    }


}