package net.antistar.logger.message;

import net.antistar.logger.AntistarLevel;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author : aruseran
 * @version 0.1
 * @since 2010. 12. 6
 */
public class AntistarMessageManager {
    private static List<Class> msgClazz = new ArrayList<Class>();
    private static boolean initialize = false;

    /**
     * 주어진 Message ID에 대한 message를 반환한다.
     * 필요한 경우 argument를 추가하여 Message의 특정 부분을 argument로 변환하여 가져올 수 있다.
     *
     * @param msgID : 가져올 Message의 ID
     * @param args : Message에서 변환될 value. 순서대로 {0}, {1}과 같은 항목에 대응된다.
     * @return : Message ID에 대한 String
     */
    public static String getMessage(Object msgID, Object... args) {
        if(msgID == null) {
            return null;
        }
        if(!initialize) {
            return msgID.toString();
        }
        if(msgID instanceof Integer) {
            for(Class messageClazz : msgClazz) {
                try {
                    Field field = messageClazz.getField("MSG_" + msgID + "_STR");
                    String message = (String) field.get(null);
                    for(int i = 0; i < args.length; i++) {
                        message = message.replace("{" + i + "}" , args[i].toString());
                    }
                    return message;
                } catch (Throwable ignored) {
                    // 그런 Field가 없을 경우 무시하고 messageID를 그대로 string으로 변환하여 반환한다. 이 경우는 주로 Message ID에 대한 오타일 확률이 높다.
                }
            }
            return String.valueOf(msgID);
        } else {
            String message = msgID.toString();
            for(int i = 0; i < args.length; i++) {
                message = message.replace("{" + i + "}" , args[i].toString());
            }
            return message;
        }
    }

    /**
     * 주어진 Message ID에 대한 Log Level을 가져온다
     * 특정 message가 어느 정도의 중요성을 갖고 있는지 설정한다.
     * Message ID가 없을 경우 info를 반환한다.
     *
     * @param msgID Log Level을 가져올 Message의 ID
     * @return msgID에 대한 Log Level
     */
    public static AntistarLevel getLevel(Object msgID) {
        if(msgID == null) {
            return AntistarLevel.INFO;
        }
        if(!initialize) {
            return AntistarLevel.INFO;
        }
        if(msgID instanceof Integer) {
            for(Class messageClazz : msgClazz) {
                try {
                    Field field = messageClazz.getField("MSG_" + msgID + "_LEVEL");
                    return (AntistarLevel) field.get(null);
                } catch (NoSuchFieldException ignored) {
                    // 그런 Field가 없을 경우 무시하고 messageID를 그대로 string으로 변환하여 반환한다. 이 경우는 주로 Message ID에 대한 오타일 확률이 높다.
                } catch (IllegalAccessException ignored) {
                    // Field가 접근할 수 없을 경우 그대로 msgID를 반환한다. 이 경우는 Message Class에 public static으로 선언되지 않았을 경우이다.
                }
            }
        }
        return AntistarLevel.INFO;
    }

    public static void init(Class... clazz) {
        initialize = true;
        msgClazz.addAll(Arrays.asList(clazz));
    }

    public static boolean isInitialized() {
        return initialize;
    }
}

