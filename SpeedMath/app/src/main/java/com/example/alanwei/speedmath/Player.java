package com.example.alanwei.speedmath;

/**
 * Created by alanwei on 12/05/15.
 */
public class Player {

    private String userName;
    private String name;

    public Player(){

    }

    public Player (String userName, String name){
        this.setUserName(userName);
        this.setName(name);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
