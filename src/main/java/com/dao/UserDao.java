package com.dao;

import com.bean.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Date;

public class UserDao implements IUserDao{
    private final MongoCollection<Document> usersCollection;
    User user = new User();

    public UserDao(MongoDatabase db) {
        this.usersCollection = db.getCollection("User");
    }
    @Override
    public User getUserByEmail(String emailid) {
        Document query = new Document("emailid", emailid);
        Document userDoc = usersCollection.find(query).first();

        if (userDoc != null) {
            // Retrieve user data from the document and create a User object
            String retrievedEmail = userDoc.getString("emailid");
            int loginAttempts = userDoc.getInteger("loginAttempts", 0);
            Date blockedUntil = userDoc.getDate("blockedUntil");
            String password = userDoc.getString("password");

            User user = new User();
            user.setEmailid(retrievedEmail);
            user.setLoginAttempt(loginAttempts);
            user.setBlockedUntil(blockedUntil);
            user.setPassword(password);
            return user;
        } else {
            // User not found in the database
            return null;
        }
    }

    @Override
    public void updateUser(User user) {
        String emailid = user.getEmailid();
        int loginAttempts = user.getLoginAttempt();
        Date blockedUntil = user.getBlockedUntil();

        Document query = new Document("emailid", emailid);

        Document updateDocument = new Document("$set", new Document()
                .append("loginAttempts", loginAttempts)
                .append("blockedUntil", blockedUntil)
        );

        usersCollection.updateOne(query, updateDocument);
    }

    public void insertUser(User user) {
        MongodbConn mongoconn = MongodbConn.getInstance();
        MongoDatabase db = mongoconn.getDatabase();
        MongoCollection<Document> collection = db.getCollection("User");
        Document userDoc = new Document("firstname", user.getFirstname())
                .append("lastname", user.getLastname())
                .append("emailid", user.getEmailid())
                .append("password", user.getPassword());
        collection.insertOne(userDoc);
    }
    public boolean doesEmailExist(String emailid){
        MongodbConn mongoconn = MongodbConn.getInstance();
        MongoDatabase db = mongoconn.getDatabase();
        MongoCollection<Document> collection = db.getCollection("User");
        Bson query = Filters.eq("emailid",emailid);
        long count = collection.countDocuments(query);
        return count>0;
    }
}
