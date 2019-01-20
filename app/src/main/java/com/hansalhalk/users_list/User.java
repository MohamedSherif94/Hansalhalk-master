package com.hansalhalk.users_list;

import org.json.simple.JSONObject;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String name;
    private String avatar;
    private String address;
    private String email;
    private String phone;
    private String facebook;
    private String twitter;
    private String instagram;
    private JSONObject category;

    public User() {
    }

    public User(int id, String name, String avatar, String address, String email, String phone, String facebook, String twitter, String instagram, JSONObject category) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.facebook = facebook;
        this.twitter = twitter;
        this.instagram = instagram;
        this.category = category;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public void setCategory(JSONObject category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getInstagram() {
        return instagram;
    }

    public JSONObject getCategory() {
        return category;
    }
}
