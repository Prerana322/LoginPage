package com.dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongodbConn {
    private static MongodbConn instance;
    private MongoClient mongoClient;
    private MongoDatabase database;

    private MongodbConn(){
        String connectingString = "mongodb://localhost:27017";
        mongoClient = MongoClients.create(connectingString);
        database = mongoClient.getDatabase("Client");
    }

    public static synchronized MongodbConn getInstance(){
        if(instance == null){
            instance = new MongodbConn();
        }
        return instance;
    }
    public MongoDatabase getDatabase(){
        return database;
    }

}

