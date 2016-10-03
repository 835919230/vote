package com.hc.dao;

import com.hc.model.Choice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by hexi on 16-10-3.
 */
@Component("choiceDao")
public interface ChoiceDao extends JpaRepository<Choice,Long> {
    @Modifying
    @Query("update Choice c set c.number=c.number+1 where c.name = :name")
    @Transactional
    int incrementNumber(@Param("name") String name);
}
