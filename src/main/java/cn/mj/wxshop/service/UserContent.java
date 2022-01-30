package cn.mj.wxshop.service;

import cn.mj.wxshop.generate.User;

public class UserContent {
    private static ThreadLocal<User> currentUser = new ThreadLocal<>();

    public static User getCurrentUser() {
        return currentUser.get();
    }

    public static void setCurrentUser(User user) {
        currentUser.set(user);
    }
}
