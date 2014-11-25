/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.inftt.dao;

import com.inftt.db.mongodb.MDB_N;
import com.inftt.db.mongodb.MongoDBClient;
import com.mongodb.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Common Data Access Object
 *
 * @author Sam Iagd
 */
public class CommonDataAccess {

    /**
     * Get email information from db.
     *
     * @param address  email address
     * @param protocol email connection protocol
     * @return email information
     */
    public static Map<String, Object> getEmailInfo(String address, String protocol) throws Exception {
        DBObject ref =
                new BasicDBObject("mail", address).append("protocol", protocol);
        DB cloud1 = MongoDBClient.get(MDB_N.CLOUD1);
        DBCollection collection = cloud1.getCollection("final_info");
        DBCursor cursor = collection.find(ref);
        if (null != cursor && cursor.hasNext()) {
            return hideRawType(cursor.next().toMap());
        }
        return new HashMap<String, Object>();
    }

    /**
     * Hide raw type warning.
     *
     * @param src source map
     * @return a map with the same entry set
     */
    static Map<String, Object> hideRawType(Map src) {
        if (null == src || src.size() < 1)
            return new HashMap<String, Object>();
        Map<String, Object> dst = new HashMap<String, Object>();
        for (Object key : src.keySet()) {
            dst.put(String.valueOf(key), src.get(key));
        }
        return dst;
    }
}
