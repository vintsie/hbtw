package com.inftt.mail;

import java.util.HashMap;
import java.util.Map;

/**
 * Mail Exception
 * Created by Sam on 11/19/2014.
 */
public class MailException extends Exception {

    public final static String ERR_CODE = "E9800";

    private final static Map<String, String> ERR_DETAIL = new HashMap<String, String>() {
        {
            put(ERR_CODE, "Email receive error.");
        }
    };

    /**
     * throw multi-lang exception, will be supported in thr future.
     *
     * @param errCode exception code.
     * @throws Exception
     */
    public MailException(String errCode) throws Exception {
        if (ERR_DETAIL.containsKey(errCode)) {
            throw new Exception(errCode + ":" + ERR_DETAIL.get(errCode));
        } else {
            throw new Exception(errCode);
        }
    }
}
