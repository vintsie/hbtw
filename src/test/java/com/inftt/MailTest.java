package com.inftt;

import com.inftt.mail.ReceiveHelper;
import org.junit.Test;

import javax.mail.Folder;
import javax.mail.MessagingException;

/**
 * Mail Testing
 * Created by Vin on 11/19/2014.
 */
public class MailTest {

    String popHost = "pop.qq.com";
    String username = "vin.is.coding@qq.com";
    String password = "caiwm@2013";

    @Test
    public void testPopReceive() {
        ReceiveHelper rh = ReceiveHelper.newPopInstance(popHost, "995", true, username, password);
        try{
            rh.setSessionDebug(true);
            Folder index = rh.getInbox(Folder.READ_ONLY);
            System.out.println(index.getMessageCount());
            System.out.println(index.getUnreadMessageCount());
        } catch (MessagingException me) {
            me.printStackTrace(System.out);
        } finally {
            rh.close();
        }


    }
}
