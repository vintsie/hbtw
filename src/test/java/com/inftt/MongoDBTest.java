package com.inftt;

import com.inftt.db.mongodb.MDB_N;
import com.inftt.db.mongodb.MongoDBClient;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import org.junit.Test;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Mongo Database Testing
 * Created by Sam on 2014/11/21.
 */
public class MongoDBTest {

    private transient static Logger log = Logger.getLogger(MongoDBTest.class.getName());

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


}
