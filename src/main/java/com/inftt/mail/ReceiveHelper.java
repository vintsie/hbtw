package com.inftt.mail;

import javax.mail.*;
import java.util.Properties;

/**
 * This is the handler of receiving email, provides the normal functions
 * of email receiving. It Supports POP3 and IMAP protocol, and sometimes
 * imap is preferred.
 * <p/>
 * Created by Sam on 11/19/2014.
 */
public abstract class ReceiveHelper {

    /**
     * email address.
     */
    private String username;

    /**
     * email password
     */
    private String password;

    /**
     * Some information relevant to email receiving will be
     * put into this props.
     */
    private Properties props = new Properties();

    Session session = null;
    Store store = null;

    boolean pulled = Boolean.FALSE;

    protected ReceiveHelper() {

    }

    /**
     * connect to remote mail server.
     *
     * @throws NoSuchProviderException
     */
    protected void connect() throws MessagingException {
        session = Session.getDefaultInstance(props, null);
        store = session.getStore();
        store.connect(username, password);
    }

    /**
     * reconnect to remote server to pull some email information.
     *
     * @throws MessagingException
     */
    protected void reconnect() throws MessagingException {
        if (null == store) store = session.getStore();
        if (store.isConnected()) {
            store.close();
        }
        store.connect(username, password);
    }

    /**
     * @param host     mail server host
     * @param isSSL    use ssl connection or not
     * @param username email address
     * @param password the pass code used to log on mail server.
     */
    protected abstract void initProps(String host, String port, boolean isSSL, String username, String password);

    /**
     * Initial user information, includes username and password.
     *
     * @param userName email address.
     * @param password the pass code used to log on mail server.
     */
    protected void initialUserInfo(String userName, String password) {
        this.username = userName;
        this.password = password;
    }

    public Folder getInbox() throws MessagingException {
        if (!pulled || !store.isConnected()) {
            connect();
        }
        return store.getFolder(MailProtocolConst.FOLDER_INDEX);
    }

    /**
     * reconnect to remote server to pull information
     *
     * @param reconnect connect remote server to pull new information
     * @return Index folder
     * @throws MessagingException
     */
    public Folder getInbox(boolean reconnect) throws MessagingException {
        reconnect();
        return store.getFolder(MailProtocolConst.FOLDER_INDEX);
    }

    public abstract Folder getFolder(String folderName) throws MessagingException;

    /**
     * Get email receiver instance, and should pass four parameters.
     *
     * @param host     mail server host
     * @param isSSL    use ssl connection or not
     * @param userName email address.
     * @param password the pass code used to log on mail server.
     * @return ReceiveHelper instance.
     */
    public static ReceiveHelper newImapInstance(String host, String port, boolean isSSL, String userName, String password) {
        ReceiveHelper rh = new ImapHandler();
        rh.initProps(host, port, isSSL, userName, password);
        return rh;
    }

    public static ReceiveHelper newPopInstance(String host, String port, boolean isSSL, String userName, String password) {
        ReceiveHelper rh = new Pop3Handler();
        rh.initProps(host, port, isSSL, userName, password);
        return rh;
    }

    /**
     * Set properties.
     *
     * @param key   property key
     * @param value property value
     */
    public void setProperty(String key, Object value) {
        this.props.put(key, value);
    }

}
