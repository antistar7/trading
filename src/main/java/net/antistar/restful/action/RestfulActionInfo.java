package net.antistar.restful.action;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * Created by IntelliJ IDEA.
 * User: syfer
 * Date: 12. 2. 24.
 * Time: 오후 12:59
 * To change this template use File | Settings | File Templates.
 */
public class RestfulActionInfo {
    private Map<String, String> actionContext = new HashMap<String, String>();
    private RestfulAction action;
    private String actionID;

    public RestfulActionInfo(RestfulActionWrapper wrapper, Matcher matcher) {
        action = wrapper.getAction();
        actionID = wrapper.getActionID();
        String[] actionContextNames = wrapper.getNames();
        int length = actionContextNames.length;
        for(int i = 0; i < length; i++) {
            actionContext.put(actionContextNames[i], matcher.group(i+1));
        }
    }

    public RestfulAction getAction() {
        return action;
    }

    public Map<String, String> getActionContext() {
        return actionContext;
    }

    public String getActionID() {
        return actionID;
    }
}
