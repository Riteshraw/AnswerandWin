package com.nubiz.answerandwin.dao;

import java.io.Serializable;

/**
 * Created by admin on 19-Jul-17.
 */

public class UserInfo implements Serializable {

    private String userName;
    private String userMobile;
    private String password;
    private String userId;
    private String requestStatus;
    private boolean isRequestPending;
    private boolean isAddedToGroup;
    String teamPlayerDetailId;

    public UserInfo() {
    }

    public UserInfo(String userName, String userId) {
        setUserName(userName);
        setUserId(userId);
    }

    public UserInfo(String text, String text1, String text2) {
        setUserName(text);
        setPassword(text1);
        setUserMobile(text2);
    }

    public String getTeamPlayerDetailId() {
        return teamPlayerDetailId;
    }

    public void setTeamPlayerDetailId(String teamPlayerDetailId) {
        this.teamPlayerDetailId = teamPlayerDetailId;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isRequestPending() {
        return isRequestPending;
    }

    public void setRequestPending(boolean requestPending) {
        isRequestPending = requestPending;
    }

    public boolean isAddedToGroup() {
        return isAddedToGroup;
    }

    public void setAddedToGroup(boolean addedToGroup) {
        isAddedToGroup = addedToGroup;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }
}
