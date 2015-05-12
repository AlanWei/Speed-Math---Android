package com.example.alanwei.speedmath;

/**
 * Created by alanwei on 12/05/15.
 */
public class Equation {
    private Integer equationID;
    private String equation;
    private Boolean answer;
    private Boolean correct;

    public Equation(){

    }

    public Equation(Integer equationID, String equation, Boolean answer, Boolean correct){
        this.setEquationID(equationID);
        this.setEquation(equation);
        this.setAnswer(answer);
        this.setCorrect(correct);
    }

    public Integer getEquationID() {
        return equationID;
    }

    public void setEquationID(Integer equationID) {
        this.equationID = equationID;
    }

    public String getEquation() {
        return equation;
    }

    public void setEquation(String equation) {
        this.equation = equation;
    }

    public Boolean isAnswer() {
        return answer;
    }

    public void setAnswer(Boolean answer) {
        this.answer = answer;
    }

    public Boolean isCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }
}
