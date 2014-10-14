package com.inftt.example.mongodb;

import com.mongodb.*;


/**
 * Study how to use MongodbClient
 * <p/>
 * Created by Vin on 2014/10/14.
 */
public class MongoDBTest {

    /**
     * 测试MongoDB客户端
     *
     * @param args Main函数入参
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++) {
            Thread.sleep(2000);
            DBCollection dbc = MongoDBInstance.getMongoDB(MDB_N.CLOUD1).getCollection("testData");
            DBCursor cursor = dbc.find();
            while (cursor.hasNext()) {
                DBObject object = cursor.next();
                System.out.println(object.toString());
            }
        }
    }
}
