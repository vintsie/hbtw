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

/**
 * Static information of mail protocol.
 * <p/>
 * Created by Sam on 11/19/2014.
 */
public class MailProtocolConst {

    public interface POP3 {
        public final static String HOST = "mail.pop3.host";
        public final static String PORT = "mail.pop3.port";
        public final static String IS_SSL = "mail.pop3.ssl.enable";
    }

    public interface IMAP {
        public final static String HOST = "mail.imap.host";
        public final static String PORT = "mail.imap.port";
        public final static String IS_SSL = "mail.imap.ssl.enable";
        public final static String PASSWORD = "password";
        public final static String USERNAME = "username";
    }

    public final static String FOLDER_INBOX = "INBOX";
    public final static String FOLDER_COMMAND = "Command";
}
