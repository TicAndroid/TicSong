package com.ticcorp.ticsong.DTO;

import java.io.Serializable;

/**
 * Created by Daesub Kim on 2016-08-17.
 */
public class FriendsScoreView  implements Serializable {

    private String userId;
    private String name;
    private int exp;
    private int userLevel;

    public FriendsScoreView() {}

    public FriendsScoreView(int userLevel, int exp, String name, String userId) {
        this.userLevel = userLevel;
        this.exp = exp;
        this.name = name;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    @Override
    public String toString() {
        return "FriendsScoreView{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", exp=" + exp +
                ", userLevel=" + userLevel +
                '}';
    }
}
