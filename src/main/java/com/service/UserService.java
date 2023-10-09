package com.service;

import com.bean.User;
import com.dao.UserDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Calendar;
import java.util.Date;

public class UserService implements IUserService{
    private static Logger logger = LogManager.getLogger(UserService.class);
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User getUserByEmail(String emailid) {
        return userDao.getUserByEmail(emailid);
    }

    @Override
    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    public boolean validatePassword(User user, String password) {
        // Retrieve the stored plain text password from the user's record in the database
        String storedpassword = user.getPassword();

        // Compare the entered plain text password with the stored plain text password
        return storedpassword.equals(password);
    }

    public void incrementLoginAttempts(User user) {
        int loginAttempts = user.getLoginAttempt() + 1;
        user.setLoginAttempt(loginAttempts);
        if (loginAttempts>= 3) {
            logger.info("User is blocked for 24 hrs if login attempts is greater than 3");
            // Block the user for 24 hours
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR_OF_DAY, 24);
            Date blockedUntil = calendar.getTime();
            user.setBlockedUntil(blockedUntil);
        }
        else{
            user.setBlockedUntil(null);
        }
        updateUser(user);
    }

    public boolean registerUser(User user){
        if(!userDao.doesEmailExist(user.getEmailid())){
            userDao.insertUser(user);
            return true;
        }
        return false;
    }

    public boolean isBlocked(String emailid) {
        User user = userDao.getUserByEmail(emailid);

        if (user != null) {
            Date blockedUntil = user.getBlockedUntil();
            if (blockedUntil != null) {
                // User is blocked
                return true;
            }
        }
        // User is not blocked or does not exist
        return false;
    }
    public int getRemainingLoginAttempts(User user){
        int loginAttempts = 3;
        return loginAttempts- user.getLoginAttempt();
    }
}
