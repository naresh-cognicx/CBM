package com.cognicx.AppointmentRemainder.response;

import java.math.BigInteger;

public class GroupNameResponse {


    private BigInteger groupId;
    private String groupName;

    public GroupNameResponse() {
    }

    public GroupNameResponse(BigInteger groupId, String groupName) {
        this.groupId=groupId;
        this.groupName=groupName;
    }

    public BigInteger getGroupId() {
        return groupId;
    }

    public void setGroupId(BigInteger groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
