package net.antistar.logger.util;

import net.antistar.logger.tx.TxRecord;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author : syfer
 * @version 0.1
 * @since 12. 7. 5.
 */
public class TxLogHelper {
    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String REMOTE_ADDRESS_HEADER_NAME = "x-forwarded-for";

    public static TxRecord createTxLogRecord(HttpServletRequest request, String apiCategory) {
        String localName;//request.getLocalName();
        try {
            localName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            localName = request.getLocalName();
        }

        TxRecord record = new TxRecord(apiCategory);
        record.setSessionKey(request.getHeader(AUTHORIZATION_HEADER_NAME));
        record.setHostName(localName);
        record.setRemoteAddress(request.getHeader(REMOTE_ADDRESS_HEADER_NAME));
        record.setRequestUri(request.getPathInfo());
        record.setRequestMethod(request.getMethod());
        /*record.setServiceCategory();
        record.setServiceAPI();*/
        //record.setFileSize(request.getContentLength());
        //record.setFileType(request.getContentType());

        return record;
    }
}
