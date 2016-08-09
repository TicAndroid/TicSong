package eu.jpark.ticsong.DTO;

import java.io.Serializable;

public class MyScoreDTO implements Serializable{


    private String resultCode;
    private String userId;
    private int exp;
    private int userLevel;


    public MyScoreDTO() {
    }

    public MyScoreDTO(int exp, String userId, int userLevel, String resultCode) {
        this.exp = exp;
        this.userId = userId;
        this.userLevel = userLevel;
        this.resultCode=resultCode;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }


    @Override
    public String toString() {
        return "MyScoreDTO{" +
                "exp=" + exp +
                ", resultCode='" + resultCode + '\'' +
                ", userId='" + userId + '\'' +
                ", userLevel=" + userLevel +
                '}';
    }
}