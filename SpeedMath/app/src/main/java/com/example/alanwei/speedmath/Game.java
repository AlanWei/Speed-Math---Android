package com.example.alanwei.speedmath;

import java.util.List;

/**
 * Created by alanwei on 12/05/15.
 */
public class Game {

    private Integer score;
    private Integer timeInSeconds;
    private long date;
    private List<Equation> equations;

    public Game() {
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getTimeInSeconds() {
        return timeInSeconds;
    }

    public void setTimeInSeconds(Integer timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public List<Equation> getEquations() {
        return equations;
    }

    public void setEquations(List<Equation> equations) {
        this.equations = equations;
    }
}
