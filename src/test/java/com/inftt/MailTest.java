package com.inftt;

import com.inftt.mail.MailProtocolConst;
import com.inftt.mail.ReceiveHelper;
import org.junit.Test;

import javax.mail.*;
import javax.mail.internet.MimeMessage;

/**
 * Mail Testing
 * Created by Sam on 11/19/2014.
 */
public class MailTest {

    String popHost = "pop.qq.com";
    String imapHost = "imap.qq.com";
    String username = "vin.is.coding@qq.com";
    String password = "caiwm@2013";

    @Test
    public void testPopReceive() {
        ReceiveHelper rh = ReceiveHelper.newPopInstance(popHost, "995", true, username, password);
        try {
            rh.setSessionDebug(true);
            int msgCount = rh.getMessageCount(MailProtocolConst.FOLDER_INBOX);
            //System.out.println(rh.getMessageCount("111"));
            //Folder index = rh.getInbox(Folder.READ_ONLY);
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
        ReceiveHelper rh = ReceiveHelper.newImapInstance(imapHost, "993", true, username, password);
        try {
            rh.setSessionDebug(false);
            rh.setMailOpMode(Folder.READ_WRITE);
            Folder inbox = rh.getFolder(MailProtocolConst.FOLDER_INBOX);
            int msgCount = inbox.getMessageCount();
            Message[] messages = inbox.getMessages(1, msgCount);
            if (null != messages && messages.length > 0) {
                for (Message message : messages) {
                    MimeMessage mMsg = (MimeMessage) message;
                    System.out.println(mMsg.getMessageID() + "|" + mMsg.getSubject());
                    if ("<AXBTYWWUW$UXUZAXUYAZXZT@mail-out.est.act-on.net>".equals(mMsg.getMessageID())) {
                        if (mMsg.getContentType().startsWith("multipart")) {
                            Multipart mp = (Multipart) mMsg.getContent();
                            int count = mp.getCount();
                            for (int i = 0; i < count; i++) {
                                BodyPart bp = mp.getBodyPart(i);
                                System.out.println(bp.getContentType());
                                System.out.println(bp.getContent().toString());
                            }
                        }
                    }
//                    String contentType = mMsg.getContentType();
//                    if(contentType.startsWith("TEXT")) {
//                        //System.out.println(mMsg.getContent().toString());
//                    }
//                    if(contentType.startsWith("multipart")) {
//                        Multipart mp = (Multipart)mMsg.getContent();
//                        int count = mp.getCount();
//                        if(count > 0){
//                            BodyPart bp = mp.getBodyPart(count);
//                            //bp.
//                        }
//                    }
                    //System.out.println(mMsg.getContentType());


/*                    BufferedInputStream bis = new BufferedInputStream(mMsg.getInputStream());
                    ByteArrayOutputStream localBuffer = new ByteArrayOutputStream();

                    byte[] temp = new byte[20480];
                    int count;
                    while ((count = bis.read(temp)) != -1) {
                        localBuffer.write(temp, 0, count);
                    }
                    System.out.println("++++:" + new String(localBuffer.toByteArray()));
                    bis.close();
                    localBuffer.close();*/
                }
            }
        } catch (Exception me) {
            me.printStackTrace(System.out);
        } finally {
            rh.close();
        }
    }


}
