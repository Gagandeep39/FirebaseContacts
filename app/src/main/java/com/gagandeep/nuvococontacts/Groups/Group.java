package com.gagandeep.nuvococontacts.Groups;

public class Group {
    int id;
    int groupName;
    int groupMembers;
    int groupMemberCount;

    public Group(int id, int groupName, int groupMembers, int groupMemberCount) {
        this.id = id;
        this.groupName = groupName;
        this.groupMembers = groupMembers;
        this.groupMemberCount = groupMemberCount;
    }


    public int getId() {
        return id;
    }

    public int getGroupName() {
        return groupName;
    }

    public int getGroupMembers() {
        return groupMembers;
    }

    public int getGroupMemberCount() {
        return groupMemberCount;
    }
}
