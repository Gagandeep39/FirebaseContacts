package com.gagandeep.nuvococontacts.Groups;

public class Group {
    int id;
    String groupName;
    String groupMembers;
    int groupMemberCount;


    public Group(int id, String groupName, String groupMembers, int groupMemberCount) {
        this.id = id;
        this.groupName = groupName;
        this.groupMembers = groupMembers;
        this.groupMemberCount = groupMemberCount;
    }

    public int getId() {
        return id;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupMembers() {
        return groupMembers;
    }

    public int getGroupMemberCount() {
        return groupMemberCount;
    }
}
