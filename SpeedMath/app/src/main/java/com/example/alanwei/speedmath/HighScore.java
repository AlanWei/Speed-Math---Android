package com.example.alanwei.speedmath;

/**
 * Created by alanwei on 11/05/15.
 */
public class HighScore {

    private Long id;
    private String name;
    private Integer highscore;
    private Integer time;

    public HighScore(){

    }

    public HighScore(Long id, String name, Integer highscore, Integer time){
        this.id = id;
        this.name = name;
        this.highscore = highscore;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHighscore() {
        return highscore;
    }

    public void setHighscore(Integer highscore) {
        this.highscore = highscore;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }
}
