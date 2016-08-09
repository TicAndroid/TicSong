package eu.jpark.ticsong.DTO;

import java.io.Serializable;

public class UserDTO implements Serializable{
    private String resultCode;
    private String userId;
    private String name;
    private int platform;

    public UserDTO() {
        // TODO Auto-generated constructor stub
    }

    public UserDTO(String userId, String name, int platform, String resultCode) {
        super();
        this.userId = userId;
        this.name = name;
        this.platform=platform;
        this.resultCode=resultCode;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
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

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "name='" + name + '\'' +
                ", resultCode='" + resultCode + '\'' +
                ", userId='" + userId + '\'' +
                ", platform=" + platform +
                '}';
    }
}