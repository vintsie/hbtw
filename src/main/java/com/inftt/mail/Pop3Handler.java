package com.inftt.mail;


import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Store;

/**
 * Created by Vin on 11/19/2014.
 */
public class Pop3Handler extends ReceiveHelper {

    @Override
    protected void initProps(String host, String port, boolean isSSL, String username, String password) {
        initialUserInfo(username, password);
        setProperty(MailProtocolConst.POP3.HOST, host);
        setProperty(MailProtocolConst.POP3.PORT, port);
        setProperty(MailProtocolConst.POP3.IS_SSL, isSSL);
    }

    @Override
    public Folder getFolder(String folderName, int mode) throws MessagingException {
        if (!MailProtocolConst.FOLDER_INBOX.equals(folderName)) {
            throw new UnsupportedOperationException();
        }
        return getInbox(mode);
    }

    @Override
    protected Store getStore() throws NoSuchProviderException {
        return session.getStore("pop3");
    }
}
