package com.dao;

import com.bean.User;

import java.util.Date;

public interface IUserDao {
    User getUserByEmail(String emailid);
    void updateUser(User user);
}
