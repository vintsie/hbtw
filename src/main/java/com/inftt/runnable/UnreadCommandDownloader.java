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
package com.inftt.runnable;

import com.inftt.mail.ReceiveHelper;
import org.apache.commons.lang3.StringUtils;

import javax.mail.Folder;
import javax.mail.MessagingException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Use the mail information given by passed parameters to pull unread message,
 * whatever I want call it command.
 * <p/>
 * Created by Vin on 2014/11/25.
 */
public class UnreadCommandDownloader extends AbstractRunnable {

    private transient static Logger logger = Logger.getLogger(UnreadCommandDownloader.class.getName());

    /**
     * Mail server information.
     */
    String host, protocol, mailAddress, password, urlInfo;

    /**
     * This variable will be mail server command folder name.
     */
    String cmdFoldName = null;
    /**
     * Mail server port.
     */
    int port;

    /**
     * Default constructor
     *
     * @param host     mail server host
     * @param protocol mail protocol
     * @param port     mail server port
     * @param address  mail account
     * @param password account password
     */
    public UnreadCommandDownloader(String host, String protocol, int port, String address, String password) {
        this.host = host;
        this.protocol = protocol;
        this.port = port;
        this.mailAddress = address;
        this.password = password;
        this.urlInfo = protocol + "//" + host + ":" + port + "/" + mailAddress;
    }

    /**
     * Use the mail information given by passed parameters to pull unread message,
     * whatever I want call it command.
     */
    @Override
    public void run() {
        ReceiveHelper helper = null;
        try {
            logger.log(Level.INFO, "New thread {0} created to pull new commands.", urlInfo);
            helper = ReceiveHelper.newImapInstance(host, String.valueOf(port), true, mailAddress, password);
            helper.setMailOpMode(Folder.READ_WRITE);
            helper.setSessionDebug(false);
            Set<String> folders = helper.listFolders("*");
            if (null != folders && folders.size() > 0) {
                for (String folder : folders) {
                    if (folder.toLowerCase().contains("/command")) {
                        cmdFoldName = folder;
                        logger.log(Level.INFO, "Command folder[{0}] selected.", cmdFoldName);
                    }
                }
            }
            if (StringUtils.isEmpty(cmdFoldName)) {
                logger.log(Level.WARNING, "No command folder found in this mail account.");
            } else {
                Folder cmdFolder = helper.getFolder(cmdFoldName);
                if (null != cmdFolder && cmdFolder.isOpen()) {
                    int msgCount = cmdFolder.getMessageCount();
                    logger.log(Level.INFO, "Command folder has {0} messages totally.", msgCount);
                    int unreadNum = cmdFolder.getUnreadMessageCount();
                    if (unreadNum > 0) {
                        logger.log(Level.INFO, "Found {0} new command(s) and start to pull to local storage.", unreadNum);
                    } else {
                        logger.log(Level.INFO, "No new command.");
                    }
                }
            }
        } catch (MessagingException me) {
            logger.log(Level.WARNING, "Request " + urlInfo + " error!", me.getCause());
        } finally {
            if (null != helper)
                helper.close();
        }
    }

    @Override
    public void setThreadInfo(String info) {
        super.setThreadInfo(urlInfo);
    }
}
