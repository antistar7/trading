package net.antistar.restful.action;

import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: syfer
 * Date: 12. 2. 23.
 * Time: 오후 10:20
 * To change this template use File | Settings | File Templates.
 */
public class RestfulActionWrapper {
    private Pattern pattern;
    private String[] names;
    private String actionID;
    private RestfulAction action;

    public RestfulActionWrapper(Pattern pattern, String[] keyNames, String actionID, RestfulAction actionInstance) {
        this.pattern = pattern;
        this.names = keyNames;
        this.actionID = actionID;
        this.action = actionInstance;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public String[] getNames() {
        return names;
    }

    public RestfulAction getAction() {
        return action;
    }

    public String getActionID() {
        return actionID;
    }
}
