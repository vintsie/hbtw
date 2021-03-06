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
package com.inftt.mail;

import java.util.HashMap;
import java.util.Map;

/**
 * Mail Exception
 * Created by Sam on 11/19/2014.
 */
public class MailException extends Exception {

    public final static String ERR_CODE = "E9800";

    private final static Map<String, String> ERR_DETAIL = new HashMap<String, String>() {
        {
            put(ERR_CODE, "Email receive error.");
        }
    };

    /**
     * throw multi-lang exception, will be supported in thr future.
     *
     * @param errCode exception code.
     * @throws Exception
     */
    public MailException(String errCode) throws Exception {
        if (ERR_DETAIL.containsKey(errCode)) {
            throw new Exception(errCode + ":" + ERR_DETAIL.get(errCode));
        } else {
            throw new Exception(errCode);
        }
    }
}
