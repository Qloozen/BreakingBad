package com.example.breakingbad;

import java.io.Serializable;
import java.util.ArrayList;

public class BBCharacter implements Serializable  {
    private String name;
    private String nickname;
    private String imgUrl;
    private String status;
    private String birthday;
    private String[] occupation;
    private int[] appearance;
    private ArrayList<String> quotes;

    public BBCharacter(String name, String nickname, String imgUrl, String status, String birthday, String[] occupation, int[] appearance, ArrayList<String> quotes) {
        this.name = name;
        this.nickname = nickname;
        this.imgUrl = imgUrl;
        this.status = status;
        this.birthday = birthday;
        this.occupation = occupation;
        this.appearance = appearance;
        this.quotes = new ArrayList<>(quotes);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String[] getOccupation() {
        return occupation;
    }

    public void setOccupation(String[] occupation) {
        this.occupation = occupation;
    }

    public int[] getAppearance() {
        return appearance;
    }

    public void setAppearance(int[] appearance) {
        this.appearance = appearance;
    }

    public ArrayList<String> getQuotes() {
        return quotes;
    }

    public void setQuotes(ArrayList<String> quotes) {
        this.quotes = new ArrayList<>(quotes);
    }

}

