package com.hc.dao;

import com.hc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by hexi on 16-10-3.
 */
@Component("userDao")
public interface UserDao extends JpaRepository<User,Long> {
    @Modifying
    @Query("update User u set u.nickname=?2 where u.id=?1")
    @Transactional(readOnly = false)
    Integer updateNicknameById(long id,String nickname);

    User findByUsername(String username);
}
