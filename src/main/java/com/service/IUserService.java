package com.service;

import com.bean.User;

import java.util.Date;

public interface IUserService {
    User getUserByEmail(String emailid);
    void updateUser(User user);
}
