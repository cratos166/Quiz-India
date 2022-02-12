package com.nbird.multiplayerquiztrivia.Model;

public class FirstTime {
    String imageURL;
    String userName;
    int firstTime,onevsoneOnlineFinder;



    public FirstTime(String imageURL, String userName, int firstTime, int onevsoneOnlineFinder) {
        this.imageURL = imageURL;
        this.userName = userName;
        this.firstTime = firstTime;
        this.onevsoneOnlineFinder = onevsoneOnlineFinder;
    }

    public FirstTime() {
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(int firstTime) {
        this.firstTime = firstTime;
    }

    public int getOnevsoneOnlineFinder() {
        return onevsoneOnlineFinder;
    }

    public void setOnevsoneOnlineFinder(int onevsoneOnlineFinder) {
        this.onevsoneOnlineFinder = onevsoneOnlineFinder;
    }
}
