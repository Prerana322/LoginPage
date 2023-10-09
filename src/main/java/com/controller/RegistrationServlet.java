package com.controller;

import com.dao.MongodbConn;
import com.dao.UserDao;
import com.bean.User;
import com.mongodb.client.MongoDatabase;
import com.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;


@WebServlet("/RegistrationServlet")
public class RegistrationServlet extends HttpServlet {
    private UserService userService;
    private static final Logger registrationlogger = LogManager.getLogger(RegistrationServlet.class);

    public void init() throws ServletException {
        // Initialize the MongoDatabase object (you should replace this with your MongoDB connection logic)
        MongodbConn mongoconn = MongodbConn.getInstance();
        MongoDatabase db = mongoconn.getDatabase();

        // Create a UserDao object and pass the database reference
        UserDao userDao = new UserDao(db);

        // Create a UserService object with the UserDao reference
        userService = new UserService(userDao);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String firstname = req.getParameter("firstname");
        String lastname = req.getParameter("lastname");
        String username = req.getParameter("username");
        String emailid = req.getParameter("emailid");
        String password = req.getParameter("password");

        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmailid(emailid);
        user.setPassword(password);

        boolean registrationsuccess = userService.registerUser(user);

        if(registrationsuccess)
        {
            registrationlogger.info("User is succesfully registered");
            res.sendRedirect("index.jsp");
        }
        else
        {
            registrationlogger.warn("User is not registered succesfully because of duplicate emailid");
            res.sendRedirect("registration.jsp?error=email-exists");
        }
    }
}

