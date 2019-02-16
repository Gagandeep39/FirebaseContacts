package com.gagandeep.nuvococontacts.Groups;

public class Group {
    int id;
    String groupName;
    String groupMembersPhone;
    String groupMembersName;
    int groupMemberCount;

    public Group(int id, String groupName, String groupMembersPhone, String groupMembersName, int groupMemberCount) {
        this.id = id;
        this.groupName = groupName;
        this.groupMembersPhone = groupMembersPhone;
        this.groupMembersName = groupMembersName;
        this.groupMemberCount = groupMemberCount;
    }


    public int getId() {
        return id;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupMembersPhone() {
        return groupMembersPhone;
    }

    public String getGroupMembersName() {
        return groupMembersName;
    }

    public int getGroupMemberCount() {
        return groupMemberCount;
    }
}
