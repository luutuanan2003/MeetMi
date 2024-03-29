package com.example.meetmi;

import java.util.List;
import java.util.Map;

public class Users {
    private String email;
    private String username;
    private String password;
    private String avatar;
    private String id;
    private double latitude;
    private double longitude;
    private Map<String, String>  friends;
    private String photoFrameId;
    private List<String> newsfeed;
    private String nickname;

    // Constructor
    public Users() {
        // Default constructor required for Firebase
    }

    public Users(String email, String username, String password, String avatar, String id, double latitude, double longitude, Map<String, String> friends, String photoFrameId, List<String> newsfeed, String nickname) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.friends = friends;
        this.photoFrameId = photoFrameId;
        this.newsfeed = newsfeed;
        this.nickname = nickname;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Map<String, String> getFriends() {
        return friends;
    }

    public void setFriends(Map<String, String> friends) {
        this.friends = friends;
    }


    public String getPhotoFrameId() {
        return photoFrameId;
    }

    public void setPhotoFrameId(String photoFrameId) {
        this.photoFrameId = photoFrameId;
    }

    public List<String> getNewsfeed() {
        return newsfeed;
    }

    public void setNewsfeed(List<String> newsfeed) {
        this.newsfeed = newsfeed;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    // To String method
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", avatar='" + avatar + '\'' +
                ", id='" + id + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", friends=" + friends +
                ", photoFrameId='" + photoFrameId + '\'' +
                ", newsfeed=" + newsfeed +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}

