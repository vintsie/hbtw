package com.inftt.mail;

import javax.mail.NoSuchProviderException;
import javax.mail.Store;

/**
 * Imap protocol mail handler.
 * <p/>
 * Created by Vin on 11/19/2014.
 */
public class ImapHandler extends ReceiveHelper {

    @Override
    protected void initProps(String host, String port, boolean isSSL, String username, String password) {
        initialUserInfo(username, password);
        setProperty(MailProtocolConst.IMAP.HOST, host);
        setProperty(MailProtocolConst.IMAP.PORT, port);
        setProperty(MailProtocolConst.IMAP.IS_SSL, isSSL);
    }


    @Override
    protected Store getStore() throws NoSuchProviderException {
        return session.getStore("imap");
    }
}
