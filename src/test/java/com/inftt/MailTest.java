package com.inftt;

import com.inftt.framework.GlobalExePool;
import com.inftt.mail.MailProtocolConst;
import com.inftt.mail.ReceiveHelper;
import com.inftt.runnable.UnreadCommandDownloader;
import org.junit.Test;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Mail Testing
 * Created by Sam on 11/19/2014.
 */
public class MailTest {

    //todo remove the password.
    String qqHost = "pop.qq.com";
    String qqImapHost = "imap.qq.com";
    String qqUsername = "vin.is.coding@qq.com";
    String qqPassword = "";

    String yeahImapHost = "imap.163.com";
    String yeahUserName = "sam_iagd@yeah.net";
    String yeahPassword = "";

    String liveImapHost = "imap-mail.outlook.com";
    String liveUserName  = "sam.iagd@hotmail.com";
    String livePassword = "";

    @Test
    public void testPopReceive() {
        ReceiveHelper rh = ReceiveHelper.newPopInstance(qqHost, "995", true, qqUsername, qqPassword);
        try {
            rh.setSessionDebug(true);
            int msgCount = rh.getMessageCount(MailProtocolConst.FOLDER_INBOX);
            Folder inbox = rh.getFolder(MailProtocolConst.FOLDER_INBOX);
            Message[] messages = inbox.getMessages(1, msgCount);
            if (null != messages && messages.length > 0) {
                for (Message message : messages) {
                    MimeMessage mMsg = (MimeMessage) message;
                    System.out.println(mMsg.getMessageID() + "|" + mMsg.getSubject());
                }
            }
        } catch (MessagingException me) {
            me.printStackTrace(System.out);
        } finally {
            rh.close();
        }
    }

    @Test
    public void testImapReceive() {
        //ReceiveHelper rh = ReceiveHelper.newImapInstance(liveImapHost, "993", true, liveUserName, livePassword);
        ReceiveHelper rh = ReceiveHelper.newImapInstance(qqImapHost, "993", true, qqUsername, qqPassword);
        try {
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
            rh.close();
        }
    }


    @Test
    public void testMailPullRunnableJob() throws Exception {
        GlobalExePool.createOnePool("m1", 5, 200, 60, TimeUnit.SECONDS);
        for (int i = 0; i < 1; i++) {
            Runnable runnable = new UnreadCommandDownloader(qqImapHost, "imap", 993, qqUsername, qqPassword);
            GlobalExePool.executeCommand("m1", runnable);
        }
        Thread.sleep(30000);
        GlobalExePool.shutdown("m1");
    }

}
