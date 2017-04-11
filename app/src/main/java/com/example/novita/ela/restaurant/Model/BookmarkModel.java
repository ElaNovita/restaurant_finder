package com.example.novita.ela.restaurant.Model;

/**
 * Created by elaa on 11/04/17.
 */

public class BookmarkModel {
    int id, user_id, cafe_id;
    boolean status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCafe_id() {
        return cafe_id;
    }

    public void setCafe_id(int cafe_id) {
        this.cafe_id = cafe_id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
