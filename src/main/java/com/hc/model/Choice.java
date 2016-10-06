package com.hc.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by hexi on 16-10-3.
 */
@Entity
@Table(name = "choice")
public class Choice implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    private String name;
    private int number;

    @ManyToOne(targetEntity = Vote.class)
    private Vote vote;

    @Column(name = "number",nullable = false)
    public int getNumber() {
        return number;
    }

    public long getId() {
        return id;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public Vote getVote() {
        return vote;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }

    public Choice() {

    }

    public void increNumber() {
        this.number ++;
    }

    @Override
    public String toString() {
        return "Choice{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", vote=" + vote +
                '}';
    }

    public Choice(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public Choice(String name, int number, Vote vote) {
        this.name = name;
        this.number = number;
        this.vote = vote;
    }
}
