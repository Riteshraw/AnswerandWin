package com.nubiz.answerandwin.dao;

/**
 * Created by admin on 04-Oct-17.
 */

public class Group {
    private String groupGuid;
    private String groupName;
    private int totalGroupMembers;
    private int pendingRequest;

    public String getGroupGuid() {
        return groupGuid;
    }

    public void setGroupGuid(String groupGuid) {
        this.groupGuid = groupGuid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getTotalGroupMembers() {
        return totalGroupMembers;
    }

    public void setTotalGroupMembers(int totalGroupMembers) {
        this.totalGroupMembers = totalGroupMembers;
    }

    public int getPendingRequest() {
        return pendingRequest;
    }

    public void setPendingRequest(int pendingRequest) {
        this.pendingRequest = pendingRequest;
    }

}
