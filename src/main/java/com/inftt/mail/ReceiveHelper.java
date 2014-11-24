package com.inftt.mail;

import javax.mail.*;
import java.util.HashMap;
import java.util.Map;
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

    private transient static Logger log = Logger.getLogger(ReceiveHelper.class.getSimpleName());

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

    Map<String, Folder> openedFolder = new HashMap<String, Folder>();
    /**
     * mail folder operation mode.
     */
    int folderOpMode = Folder.READ_ONLY;

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
        if (null == store)
            store = getStore();
        store.connect(username, password);
    }

    /**
     * Check mail server store is connected or not.
     *
     * @return if local store is connecting to remote server, return
     * true, or return false.
     * @throws MessagingException
     */
    protected boolean isConnected() throws MessagingException {
        return null != session && null != store && store.isConnected();
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
     * Initial mail server properties.
     *
     * @param host     mail server host
     * @param port     mail server port
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

    /**
     * Open inbox folder with the specified open mode.
     *
     * @param mode open mode.
     * @return Folder
     * @throws MessagingException
     */
//    public Folder getInbox(int mode) throws MessagingException {
//        if (!pulled || !store.isConnected()) {
//            connect();
//        }
//        Folder folder = store.getFolder(MailProtocolConst.FOLDER_INBOX);
//        folder.open(mode);
//        openedFolder.put(MailProtocolConst.FOLDER_INBOX, folder);
//        return folder;
//    }

    /**
     * reconnect to remote server to pull information
     *
     * @param reconnect connect remote server to pull new information
     * @return Index folder
     * @throws MessagingException
     */
//    public Folder getInbox(boolean reconnect, int mode) throws MessagingException {
//        if (reconnect)
//            reconnect();
//        return getInbox(mode);
//    }

    /**
     * close store and folder
     */
    public void close() {
        try {
            if (store != null && store.isConnected()) {
                store.close();
            }
            for (Map.Entry<String, Folder> entry : openedFolder.entrySet()) {
                if (entry.getValue().isOpen()) {
                    entry.getValue().close(false);
                }
            }
        } catch (MessagingException me) {
            if (log.isLoggable(Level.WARNING)) {
                log.warning("close session failed.");
            }
        }

    }

    /**
     * Open mail folder by folder name and open-mode
     *
     * @param folderName mail box folder name
     * @return mail box folder
     * @throws MessagingException
     */
    public Folder getFolder(String folderName) throws MessagingException {
        if (openedFolder.containsKey(folderName)) {
            return openedFolder.get(folderName);
        }
        if (null == store || !store.isConnected()) {
            connect();
        }
        Folder folder = store.getFolder(folderName);
        folder.open(folderOpMode);
        openedFolder.put(folderName, folder);
        return folder;
    }

    /**
     * Get Message count by folder name.
     *
     * @param folderName mail box folder name
     * @return message count
     * @throws MessagingException
     */
    public int getMessageCount(String folderName) throws MessagingException {
        Folder folder = openedFolder.get(folderName);
        if (null == folder || !folder.isOpen()) {
            folder = getFolder(folderName);
        }
        return folder.getMessageCount();
    }

    /**
     * get namespaces of local store which is mapping to remote server.
     *
     * @return namespaces of local store which is mapping to remote server.
     * @throws MessagingException
     */
    public Folder[] getPersonalNamespaces() throws MessagingException {
        if (!isConnected())
            connect();
        return store.getPersonalNamespaces();
    }

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
     * set mail folder operation mode, currently read_only and read_write
     * are supported.
     *
     * @param opMode operation mode
     */
    public void setMailOpMode(int opMode) {
        if (Folder.READ_ONLY == opMode || Folder.READ_WRITE == opMode)
            this.folderOpMode = opMode;
        else
            throw new UnsupportedOperationException("unknown folder operation mode " + opMode);
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
