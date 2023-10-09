package com.controller;

import com.bean.User;
import com.dao.MongodbConn;
import com.dao.UserDao;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.service.UserService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private UserService userService;
    private static final Logger servletLogger = LogManager.getLogger(LoginServlet.class);

    @Override
    public void init() throws ServletException {
        // Initialize the MongoDatabase object (you should replace this with your MongoDB connection logic)
        MongodbConn mongoconn = MongodbConn.getInstance();
        MongoDatabase db = mongoconn.getDatabase();

        // Create a UserDao object and pass the database reference
        UserDao userDao = new UserDao(db);

        // Create a UserService object with the UserDao reference
        userService = new UserService(userDao);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws  ServletException, IOException {
        // Retrieve the values entered by the user in the login form
        String emailid = request.getParameter("emailid");
        String password = request.getParameter("password");
        PrintWriter out = response.getWriter();
        servletLogger.info("doPost Called");

        // Query your database or data source to find the user by username
        User user = userService.getUserByEmail(emailid);
        System.out.println(user.getEmailid());

        if (user != null) {
            // Check if the user is blocked
            if (!userService.isBlocked(emailid)) {
                // Verify the user's password
                if (userService.validatePassword(user, password)) {
                    // Successful login
                    user.setLoginAttempt(0); // Reset login attempts
                    user.setBlockedUntil(null); // Reset blocked status
                    userService.updateUser(user);

                    servletLogger.info("Successfull login for user with emailid:"+emailid);

                    request.setAttribute("firstname",user.getFirstname());
                    request.setAttribute("emailid",user.getEmailid());
                    RequestDispatcher dispatcher=request.getRequestDispatcher("Welcome.jsp");
                    dispatcher.forward(request, response);
                    //response.sendRedirect("Welcome.jsp");
                    return;
                } else {
                    // Incorrect password, increment login attempts
                    userService.incrementLoginAttempts(user);
                    request.setAttribute("message","Attempts remaining is:"+userService.getRemainingLoginAttempts(user));
                    RequestDispatcher dispatcher=request.getRequestDispatcher("error.jsp");
                    dispatcher.forward(request, response);
                    //out.print("Attempts remaining is:"+userService.getRemainingLoginAttempts(user));
                    servletLogger.warn("User entered incorrect password");
                }
            } else {
                // User is blocked
                String message = "User is blocked";
                request.setAttribute("message",message);
                RequestDispatcher dispatcher=request.getRequestDispatcher("error.jsp");
                dispatcher.forward(request, response);
                //request.setAttribute("error", "User is blocked until " + user.getBlockedUntil());
                servletLogger.warn("User is blocked");
            }
        } else {
            // User not found
            request.setAttribute("error", "Invalid username or password");
            RequestDispatcher dispatcher=request.getRequestDispatcher("error.jsp");
            dispatcher.forward(request, response);
            servletLogger.warn("User not found");
        }

        // Display login form with error message
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

}
