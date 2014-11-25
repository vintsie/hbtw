package com.inftt;

import com.inftt.dao.CommonDataAccess;
import com.inftt.framework.GlobalExePool;
import com.inftt.mail.ReceiveHelper;
import com.inftt.runnable.UnreadCommandDownloader;
import org.apache.commons.collections4.MapUtils;
import org.junit.Test;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Mail Testing
 * Created by Sam on 11/19/2014.
 */
public class MailTest {
    String qqUsername = "vin.is.coding@qq.com";


    @Test
    public void testImapReceive() {

        //ReceiveHelper rh = ReceiveHelper.newImapInstance(liveImapHost, "993", true, liveUserName, livePassword);
        ReceiveHelper rh = null;
        try {
            Map<String, Object> mailInfo = CommonDataAccess.getEmailInfo(qqUsername, "imap");
            rh = ReceiveHelper.newImapInstance(
                    MapUtils.getString(mailInfo, "host"),
                    MapUtils.getString(mailInfo, "port"),
                    MapUtils.getBoolean(mailInfo, "isSSL"),
                    MapUtils.getString(mailInfo, "mail"),
                    MapUtils.getString(mailInfo, "password"));

            rh.setSessionDebug(false);
            rh.setMailOpMode(Folder.READ_WRITE);

            Set<String> folderNames = rh.listFolders("*");
            String cFolderName = null;
            for(String folderName : folderNames) {
                System.out.println(folderName);
                if(folderName.contains("/Command")) {
                    cFolderName = folderName;
                }
            }

            Folder inbox = rh.getFolder(cFolderName);
            int msgCount = inbox.getUnreadMessageCount();
            Message[] messages = inbox.getMessages(1, msgCount);
            if (null != messages && messages.length > 0) {
                for (Message message : messages) {
                    MimeMessage mMsg = (MimeMessage) message;
                    System.out.println(mMsg.getMessageID() + "|" + mMsg.getSubject());
                    System.out.println(mMsg.getContentType());
                    System.out.println(mMsg.getContent().getClass().getName());
                }
            }
        } catch (Exception me) {
            me.printStackTrace(System.out);
        } finally {
            if(null != rh)
            rh.close();
        }
    }


    @Test
    public void testMailPullRunnableJob() throws Exception {
        GlobalExePool.createOnePool("m1", 5, 200, 60, TimeUnit.SECONDS);
        Map<String, Object> mailInfo = CommonDataAccess.getEmailInfo(qqUsername, "imap");
        for (int i = 0; i < 10; i++) {
            Runnable runnable = new UnreadCommandDownloader(MapUtils.getString(mailInfo, "host"), "imap", 993,
                    MapUtils.getString(mailInfo, "mail"),
                    MapUtils.getString(mailInfo, "password"));
            GlobalExePool.executeCommand("m1", runnable);
            Thread.sleep(20000);
        }
        Thread.sleep(30000);
        GlobalExePool.shutdown("m1");
    }

}
