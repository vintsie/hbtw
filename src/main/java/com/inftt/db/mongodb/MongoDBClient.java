package com.inftt.db.mongodb;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import java.util.ArrayList;
import java.util.List;

/**
 * MongoDB
 * <p/>
 * Created by Sam on 2014/11/14.
 */
public class MongoDBClient {

    public final static String HOST = "127.0.0.1";
    public final static int PORT = 27017;

    final static String[][] credentialMatrix = new String[][]{
            {"mgex", "cloud_0x0001", "mgex"},
            {"mgex", "cloud_0x0002", "mgex"}
    };

    /**
     * create mongodb connection credential.
     *
     * @return credential
     * @throws Exception
     */
    private static List<MongoCredential> createCredentials() throws Exception {
        List<MongoCredential> credentialList = new ArrayList<MongoCredential>();
        MongoCredential credential;
        for (String[] _credential : credentialMatrix) {
            credential = MongoCredential.createMongoCRCredential(
                    _credential[0], _credential[1], _credential[2].toCharArray());
            credentialList.add(credential);
        }
        return credentialList;
    }

    /**
     * get database instance handler
     *
     * @param dbName database name
     * @return database handler
     * @throws Exception
     */
    public static DB get(String dbName) throws Exception {
        ServerAddress address = new ServerAddress(HOST, PORT);
        return new MongoClient(address, createCredentials()).getDB(dbName);
    }
}
