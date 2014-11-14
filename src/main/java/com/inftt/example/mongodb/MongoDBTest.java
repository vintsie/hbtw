package com.inftt.example.mongodb;

import com.mongodb.*;
import org.junit.Test;


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
        //Thread.sleep(2000);
        DBCollection dbc = MongoDBInstance.getMongoDB(MDB_N.CLOUD1).getCollection("testData");
        dbc.drop();
        //DBCursor cursor = dbc.find();
        //while (cursor.hasNext()) {
            //DBObject object = cursor.next();
            //String objectId = String.valueOf(object.get("_id"));
            //dbc.remove(object);
            //System.out.println("Object [" + objectId + "] removed.");
        //}
        //DBObject object = new BasicDBObject();
        //object.put("dept_id", "D90021");
        //object.put("dept_name", "SECURE DEP.");
        //object.put("master", "Henrik Rails");
        //dbc.insert(object);
    }

    @Test
    public void testRichObjectInsertion() throws Exception {
        DBObject object = new BasicDBObject("title", "Purchase Order 1").append("total", 10.50);
        DBObject lineItem = new BasicDBObject("sku", "a").append("quantity", 1).append("price", 10.50);
        BasicDBList dbList = new BasicDBList();
        dbList.add(lineItem);

        object.put("line_items", dbList);
        //MongoDBInstance.getMongoDB(MDB_N.CLOUD1).createCollection("purchase_list", null);
        MongoDBInstance.getMongoDB(MDB_N.CLOUD1).getCollection("purchase_list").insert(object);
    }

    @Test
    public void testModifyDBObject() throws Exception{
        //DBCursor cursor = MongoDBInstance.getMongoDB(MDB_N.CLOUD1)
        //        .getCollection("purchase_list").find(new BasicDBObject("title", "Purchase Order 1"));

        DBCollection collection = MongoDBInstance.getMongoDB(MDB_N.CLOUD1).getCollection("purchase_list");
        collection.update(new BasicDBObject("title", "Purchase Order 2"),
                new BasicDBObject("$set", new BasicDBObject("title", "Purchase Order 3")));

        //while(cursor.hasNext()){
        ///    System.out.println(cursor.next());
        //}
    }
}
