package com.inftt.mail;

import com.sun.mail.imap.IMAPSSLStore;

import javax.mail.*;

/**
 * Imap protocol mail handler.
 * <p/>
 * Created by Sam on 11/19/2014.
 */
public class ImapHandler extends ReceiveHelper {

    /**
     * Get folders under user namespace,
     * override from {@link com.inftt.mail.ReceiveHelper#getNameSpaces(String)}
     *
     * @param username username
     * @return Folders under user namespace.
     * @throws MessagingException
     */
    @Override
    public Folder[] getNameSpaces(String username) throws MessagingException {
        if (!isConnected()) {
            connect();
        }
        return store.getUserNamespaces(username);
    }

    @Override
    protected void initProps(String host, String port, boolean isSSL, String username, String password) {
        initialUserInfo(username, password);
        setProperty(MailProtocolConst.IMAP.HOST, host);
        setProperty(MailProtocolConst.IMAP.PORT, port);
        setProperty(MailProtocolConst.IMAP.IS_SSL, isSSL);
        setProperty(MailProtocolConst.IMAP.USERNAME, username);
        setProperty(MailProtocolConst.IMAP.PASSWORD, password);
    }


    @Override
    protected Store getStore() throws NoSuchProviderException {
        URLName urlName = new URLName(
                "imap",
                props.getProperty(MailProtocolConst.IMAP.HOST),
                Integer.parseInt(props.getProperty(MailProtocolConst.IMAP.PORT, "-1")),
                null,
                props.getProperty(MailProtocolConst.IMAP.USERNAME),
                props.getProperty(MailProtocolConst.IMAP.PASSWORD));
        return new IMAPSSLStore(session, urlName);
    }
}
