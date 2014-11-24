package com.inftt.mail;


import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Store;

/**
 * POP3 protocol mail handler.
 * <p/>
 * Created by Vin on 11/19/2014.
 */
public class Pop3Handler extends ReceiveHelper {

    /**
     * Initial pop3 protocol properties.
     *
     * @param host     mail server host
     * @param port     mail server port
     * @param isSSL    use ssl connection or not
     * @param username email address
     * @param password the pass code used to log on mail server.
     */
    @Override
    protected void initProps(String host, String port, boolean isSSL, String username, String password) {
        initialUserInfo(username, password);
        setProperty(MailProtocolConst.POP3.HOST, host);
        setProperty(MailProtocolConst.POP3.PORT, port);
        setProperty(MailProtocolConst.POP3.IS_SSL, isSSL);
    }

    /**
     * Open mail folder. Only inbox folder can be requested to open while
     * using Pop3 protocol.
     *
     * @param folderName mail box folder name
     * @return Mail Folder, under this protocol means inbox.
     * @throws MessagingException
     */
    @Override
    public Folder getFolder(String folderName) throws MessagingException {
        if (!MailProtocolConst.FOLDER_INBOX.equals(folderName)) {
            throw new UnsupportedOperationException("POP3 only can open inbox folder.");
        }
        return super.getFolder(MailProtocolConst.FOLDER_INBOX);
    }

    /**
     * Only inbox folder can be requested to open using Pop3 protocol.
     *
     * @param folderName mail box folder name
     * @return message count
     * @throws MessagingException
     */
    @Override
    public int getMessageCount(String folderName) throws MessagingException {
        if (!MailProtocolConst.FOLDER_INBOX.equals(folderName)) {
            throw new UnsupportedOperationException("POP3 only can open inbox folder.");
        }
        return super.getMessageCount(folderName);
    }

    @Override
    protected Store getStore() throws NoSuchProviderException {
        return session.getStore("pop3");
    }

    /**
     * Pop3 protocol only support read_only mode.
     *
     * @param opMode operation mode
     */
    @Override
    public void setMailOpMode(int opMode) {
        if(Folder.READ_ONLY == opMode)
            super.setMailOpMode(opMode);
        else
            throw new UnsupportedOperationException("Pop3 protocol only support read_only mode.");
    }
}
