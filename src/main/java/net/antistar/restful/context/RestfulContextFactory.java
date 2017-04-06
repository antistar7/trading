package net.antistar.restful.context;

import net.antistar.logger.json.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: syfer
 * Date: 12. 2. 24.
 * Time: 오후 6:37
 * To change this template use File | Settings | File Templates.
 */
public class RestfulContextFactory {
    public static RestfulContext createContext(JSONObject contextObj) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return (RestfulContext) Class.forName(contextObj.getString("class")).newInstance();
    }
}
