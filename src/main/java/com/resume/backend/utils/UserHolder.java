package com.resume.backend.utils;


import com.resume.backend.entity.User;

public class UserHolder {

    private static final ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public static void saveUser(User user){
        userThreadLocal.set(user);
    }

    public static User getUser(){
        return userThreadLocal.get();
    }

    public static void removeUser(){
        userThreadLocal.remove();
    }

}