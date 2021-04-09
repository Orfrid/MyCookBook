package com.cookbook.Model;

public class CurrentUser {

    public final static CurrentUser instance = new CurrentUser();
    User user = new User();

    private CurrentUser() {
    }

    public User getUser() {
        return this.user;
    }
}
