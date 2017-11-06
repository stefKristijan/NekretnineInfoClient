package com.em2.kstefancic.nekretnineinfo.api.model;

import java.io.Serializable;

/**
 * Created by user on 2.11.2017..
 */

public class User implements Serializable{

    public enum Role {
        ADMIN, USER;
    }

    private Long id;
    private String uId;
    private String mFirstName;
    private String mLastName;
    private String mUsername;
    private String mPassword;
    private String mEmail;
    private Role mRole;

    public User(){}

    public User(String uid, String mFirstName, String mLastName, String mUsername, String mPassword, String mEmail) {
        this.uId = uid;
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
        this.mUsername = mUsername;
        this.mPassword = mPassword;
        this.mEmail = mEmail;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", uId='" + uId + '\'' +
                ", mFirstName='" + mFirstName + '\'' +
                ", mLastName='" + mLastName + '\'' +
                ", mUsername='" + mUsername + '\'' +
                ", mPassword='" + mPassword + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mRole='" + mRole + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return uId.equals(user.uId);
    }

    @Override
    public int hashCode() {
        return uId.hashCode();
    }

    public Role getmRole() {
        return mRole;
    }

    public void setmRole(Role mRole) {
        this.mRole = mRole;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getmFirstName() {
        return mFirstName;
    }

    public void setmFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getmLastName() {
        return mLastName;
    }

    public void setmLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }
}
