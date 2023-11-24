package com.example.ass2_i200802_i200707;

public class CurrentUserDataHolder {

    private static CurrentUserDataHolder instance;
    private User user;

    private CurrentUserDataHolder() {
        // Initialize your User object when the class is instantiated
        user = new User();
    }

    public static synchronized CurrentUserDataHolder getInstance() {
        if (instance == null) {
            instance = new CurrentUserDataHolder();
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
