package com.gagandeep.nuvococontacts;

public class FavouriteItem {
    int _id;
    String name;
    String phoneno;

    public FavouriteItem(int _id, String name, String phoneno) {
        this._id = _id;
        this.name = name;
        this.phoneno = phoneno;
    }

    FavouriteItem() {
    }

    public int get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneno() {
        return phoneno;
    }


}
