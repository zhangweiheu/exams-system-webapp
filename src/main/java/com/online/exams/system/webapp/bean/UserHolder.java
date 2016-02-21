package com.online.exams.system.webapp.bean;

import com.online.exams.system.core.model.User;

/**
 * Created by quxiao on 2015/4/27.
 */
public class UserHolder {

    private static UserHolder instance;

    public static UserHolder getInstance() {
        if (instance == null) {
            synchronized (UserHolder.class) {
                if (instance == null) {
                    instance = new UserHolder();
                }
            }
        }

        return instance;
    }

    ThreadLocal<User> repo;

    public UserHolder() {
        repo = new ThreadLocal<>();
    }

    public User getUser() {
        return repo.get();
    }

    public void delete() {
        repo.remove();
    }

    public void set(User user) {
        repo.set(user);
    }
}
