package com.inftt.example.mongodb;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Mongodb client singleton.
 * <p/>
 * Created by Vin on 2014/10/14.
 */
public class MongoDBInstance {

    static Logger logger = Logger.getLogger(MongoDBInstance.class.getName());

    public final static String HOST = "127.0.0.1";
    public final static int PORT = 27017;

    static MongoClient _mc = null;
    final static String[][] credentialMatrix = new String[][]{
            {"mgex", "cloud_0x0001", "mgex"},
            {"mgex", "cloud_0x0002", "mgex"}
    };

    /**
     * forbidden to call constructor outside of this class.
     */
    private MongoDBInstance() {
    }

    /**
     * Get cached Mongodb Client
     *
     * @return MongoClient
     */
    public static MongoClient getInstance() {
        return _mc;
    }

    /**
     * Get database handler by database name.
     *
     * @param dbName database name
     * @return db api layer
     */
    public static DB getMongoDB(String dbName) {
        return getInstance().getDB(dbName);
    }

    static {
        try {
            ServerAddress address = new ServerAddress(HOST, PORT);
            List<MongoCredential> credentialList = new ArrayList<MongoCredential>();
            MongoCredential credential;
            for (String[] _credential : credentialMatrix) {
                credential = MongoCredential.createMongoCRCredential(
                        _credential[0], _credential[1], _credential[2].toCharArray());
                credentialList.add(credential);
            }
            _mc = new MongoClient(address, credentialList);
        } catch (Exception e) {
            _mc = null;
            e.printStackTrace();
        }
    }
}
