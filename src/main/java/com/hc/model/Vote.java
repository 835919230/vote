package com.hc.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Created by hexi on 16-10-3.
 */
@Entity
@Table(name = "vote")
public class Vote implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    private String description;
    private int participateNumber;
    private boolean isMultiple;
    private Date createTime;
    private Date startTime;
    private Date endTime;

    @ManyToOne(targetEntity = User.class)
    private User user;

    @OneToMany(targetEntity = Choice.class, mappedBy = "vote", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Choice> choices;

    public Vote() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "title", nullable = false, length = 40)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "description",nullable = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setParticipateNumber(int participateNumber) {
        this.participateNumber = participateNumber;
    }

    @Column(name = "paticipate_number",nullable = false)
    public int getParticipateNumber() {
        return participateNumber;
    }

    public void setMultiple(boolean multiple) {
        isMultiple = multiple;
    }

    @Column(name = "is_multiple")
    public boolean isMultiple() {
        return isMultiple;
    }

    @Column(name = "create_time",nullable = false)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "start_time",nullable = false)
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Column(name = "end_time",nullable = false)
    public Date getEndTime() {
        return endTime;
    }

    public Set<Choice> getChoices() {
        return choices;
    }

    public void setChoices(Set<Choice> choices) {
        this.choices = choices;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createTime=" + createTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", user=" + user +
                '}';
    }

    public void increParNumber() {
        this.participateNumber ++;
    }

    public Vote(String title,
                String description,
                int participateNumber,
                boolean isMultiple,
                Date createTime, Date startTime, Date endTime) {
        this.title = title;
        this.description = description;
        this.participateNumber = participateNumber;
        this.isMultiple = isMultiple;
        this.createTime = createTime;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
