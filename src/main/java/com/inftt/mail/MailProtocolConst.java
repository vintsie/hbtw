package com.inftt.mail;

/**
 * Static information of mail protocol.
 * <p/>
 * Created by Sam on 11/19/2014.
 */
public class MailProtocolConst {

    public interface POP3 {
        public final static String HOST = "mail.pop3.host";
        public final static String PORT = "mail.pop3.port";
        public final static String IS_SSL = "mail.pop3.ssl.enable";
    }

    public interface IMAP {
        public final static String HOST = "mail.imap.host";
        public final static String PORT = "mail.imap.port";
        public final static String IS_SSL = "mail.imap.ssl.enable";
        public final static String PASSWORD = "password";
        public final static String USERNAME = "username";
    }

    public final static String FOLDER_INBOX = "INBOX";
    public final static String FOLDER_COMMAND = "Command";
}
