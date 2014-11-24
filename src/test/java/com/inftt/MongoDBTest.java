package com.inftt;

import com.inftt.db.mongodb.MDB_N;
import com.inftt.db.mongodb.MongoDBClient;
import com.mongodb.*;
import org.junit.Test;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Mongo Database Testing
 * Created by Sam on 2014/11/21.
 */
public class MongoDBTest {

    private transient static Logger log = Logger.getLogger(MongoDBTest.class.getSimpleName());

    /**
     * Test mongo database client
     *
     * @throws Exception
     */
    @Test
    public void testMongoDBClient() throws Exception {
        DB cloud1 = MongoDBClient.get(MDB_N.CLOUD1);
        Set<String> allCols = cloud1.getCollectionNames();
        if (null != allCols && allCols.size() > 0) {
            for (String colName : allCols) {
                if (colName.startsWith("system."))
                    continue;
                DBCollection dbc = cloud1.getCollection(colName);
                log.log(Level.WARNING, "${0} will be removed.", new Object[]{dbc.getFullName()});
                dbc.drop();
            }
        }
    }


    @Test
    public void testCreateMongoDBCollection() throws Exception {
        DB cloud1 = MongoDBClient.get(MDB_N.CLOUD1);
        DBCollection collection = cloud1.createCollection("mail_test", null);
        DBObject object = new BasicDBObject();
        object.put("content_type", "TEXT/PLAIN");
        object.put("size", 100280);

        DBObject object1 = new BasicDBObject();
        object1.put("content_type", "multipart");
        object.put("sub_content", object1);

        collection.insert(object);
    }

    @Test
    public void testModifyData() throws Exception {
        DB cloud1 = MongoDBClient.get(MDB_N.CLOUD1);
        DBCollection collection = cloud1.getCollection("mail_test");
        DBCursor cursor = collection.find();
        while (cursor.hasNext()) {
            DBObject obj = cursor.next();
            obj.removeField("size");
            collection.save(obj);
        }
    }


}
