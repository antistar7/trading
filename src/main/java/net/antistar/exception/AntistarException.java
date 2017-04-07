package net.antistar.exception;

import net.antistar.logger.message.AntistarMessageManager;

/**
 * @author : aruseran
 * @version 0.1
 * @since 2010. 9. 14
 */
public class AntistarException extends Exception {
    public AntistarException(String message, Exception e) {
        super(message, e);
    }

    public AntistarException(int msgID) {
        super(AntistarMessageManager.getMessage(msgID));
    }

    public AntistarException(int msgID, Object... args) {
        super(AntistarMessageManager.getMessage(msgID, args));
    }

    public AntistarException(int msgID, Exception e) {
        super(AntistarMessageManager.getMessage(msgID), e);
    }

    public AntistarException(int msgID, Exception e, Object... args) {
        super(AntistarMessageManager.getMessage(msgID, args), e);
    }

    public AntistarException(Exception e) {
        super(e);
    }

    public AntistarException(String message) {
        super(message);
    }
}

