package net.antistar.restful.exception;

import net.antistar.exception.AntistarException;

/**
 * Exception for Http Transaction log
 *
 * @author : yongsoo.hur@kt.com
 * @version 0.1
 * @since 12. 7. 18.
 */
public class UCloudHttpException extends AntistarException {
    private int responseCode;
    private int errorCode;

    public UCloudHttpException(int responseCode, int errorCode, String message) {
        super(message);
        this.responseCode = responseCode;
        this.errorCode = errorCode;
    }

    public UCloudHttpException(int responseCode, int errorCode, String message, Exception e) {
        super(message, e);
        this.responseCode = responseCode;
        this.errorCode = errorCode;
    }

    public UCloudHttpException(int responseCode, int errorCode, int msgID) {
        super(msgID);
        this.responseCode = responseCode;
        this.errorCode = errorCode;
    }

    public UCloudHttpException(int responseCode, int errorCode, int msgID, Object... args) {
        super(msgID, args);
        this.responseCode = responseCode;
        this.errorCode = errorCode;
    }

    public UCloudHttpException(int responseCode, int errorCode, int msgID, Exception e) {
        super(msgID, e);
        this.responseCode = responseCode;
        this.errorCode = errorCode;
    }

    public UCloudHttpException(int responseCode, int errorCode, int msgID, Exception e, Object... args) {
        super(msgID, e, args);
        this.responseCode = responseCode;
        this.errorCode = errorCode;
    }

    public UCloudHttpException(int responseCode, int errorCode, Exception e) {
        super(e);
        this.responseCode = responseCode;
        this.errorCode = errorCode;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
