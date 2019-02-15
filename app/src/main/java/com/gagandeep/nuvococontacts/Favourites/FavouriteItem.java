package com.gagandeep.nuvococontacts.Favourites;

public class FavouriteItem {
    int _id;
    String name;
    String phoneno;

    //Custom datatype for Local SQLite database
    public FavouriteItem(int _id, String name, String phoneno) {
        this._id = _id;
        this.name = name;
        this.phoneno = phoneno;
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
