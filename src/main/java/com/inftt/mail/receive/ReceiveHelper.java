package com.inftt.mail.receive;

import javax.mail.*;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * receive email helper.
 * Created by Vin on 2014/11/19.
 */
public class ReceiveHelper {
    private transient static Logger log = Logger.getLogger(ReceiveHelper.class.getName());

    public static void main(String[] args) {
        Store store = null;

        String host = "pop.qq.com";
        String username = "vin.is.coding@qq.com";
        String password = "caiwm@2013";

//        String host = "pop.163.com";
//        String username = "sam_iagd@yeah.net";
//        String password = "caiwm@2014";

        Folder inbox = null;
        try {
            Properties props = new Properties();
            props.put("mail.pop3.port", 995);
            props.put("mail.pop3.host", host);
            props.put("mail.pop3.ssl.enable", true);
            // get session
            Session session = Session.getDefaultInstance(props, null);
            //session.setDebug(true);
            // get the store
            store = session.getStore("pop3");

            store.connect(username, password);

            // get folder
            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);
            int msgCount = inbox.getMessageCount();
            int unreadMsgCount = inbox.getUnreadMessageCount();
            if (log.isLoggable(Level.INFO)) {
                log.info("message count " + msgCount);
                //log.info(inbox.);
                log.info("unread message count " + unreadMsgCount);
            }
            Message[] messages = inbox.getMessages(1, msgCount);
            //Message[] allMsgs = inbox.getMessages();
            //System.out.println(allMsgs.length);
            if (null != messages && messages.length > 0) {
                int i = 1;
                for (Message message : messages) {
                    //if(log.isLoggable(Level.INFO)) {
                    System.out.println((i++) + "\t" + message.getSubject() + "\t" + new Timestamp(message.getSentDate().getTime()));
                    message.setFlag(Flags.Flag.SEEN, true);
                    //message.
                    //System.out.println(message.);
                    //}
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace(System.out);
        } finally {
            try {
                if (null != store) {
                    store.close();
                }
                if (null != inbox && inbox.isOpen()) {
                    inbox.close(false);
                }
            } catch (MessagingException me) {
                me.printStackTrace(System.out);
            }
        }
    }
}
