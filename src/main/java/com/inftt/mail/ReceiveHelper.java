package com.inftt.mail;

import javax.mail.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the handler of receiving email, provides the normal functions
 * of email receiving. It Supports POP3 and IMAP protocol, and sometimes
 * imap is preferred.
 * <p/>
 * Created by Sam on 11/19/2014.
 */
public abstract class ReceiveHelper {

    private transient static Logger log = Logger.getLogger(ReceiveHelper.class.getName());

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

    List<Folder> openedFolder = new ArrayList<Folder>();

    boolean pulled = Boolean.FALSE;

    protected ReceiveHelper() {

    }

    /**
     * connect to remote mail server.
     *
     * @throws NoSuchProviderException
     */
    protected void connect() throws MessagingException {
        if (null == session)
            session = Session.getDefaultInstance(props, null);
        store = getStore();
        store.connect(username, password);
    }

    protected abstract Store getStore() throws NoSuchProviderException;

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

    public Folder getInbox(int mode) throws MessagingException {
        if (!pulled || !store.isConnected()) {
            connect();
        }
        Folder folder = store.getFolder(MailProtocolConst.FOLDER_INBOX);
        folder.open(mode);
        openedFolder.add(folder);
        return folder;
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
        return store.getFolder(MailProtocolConst.FOLDER_INBOX);
    }

    /**
     * close store and folder
     *
     * @throws MessagingException
     */
    public void close() {
        try {
            if (store != null && store.isConnected()) {
                store.close();
            }
            for (Folder folder : openedFolder) {
                if (folder.isOpen()) {
                    folder.close(false);
                }
            }
        } catch (MessagingException me) {
            if (log.isLoggable(Level.WARNING)) {
                log.warning("close session failed.");
            }
        }

    }

    public abstract Folder getFolder(String folderName, int mode) throws MessagingException;

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

    public void setSessionDebug(boolean debug) {
        if (null == session) {
            session = Session.getDefaultInstance(props, null);
        }
        session.setDebug(debug);
    }

}
