package net.antistar.restful.action;

import net.antistar.logger.json.JSONArray;
import net.antistar.logger.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: syfer
 * Date: 12. 2. 23.
 * Time: 오후 9:08
 * To change this template use File | Settings | File Templates.
 */
public class RestfulActionFactory {
    private Map<String, List<RestfulActionWrapper>> actionMap = new HashMap<String, List<RestfulActionWrapper>>();

    public static RestfulActionFactory createActionFactory(JSONObject api) {
        return new RestfulActionFactory(api);
    }

    private RestfulActionFactory(JSONObject api) {
        JSONArray apiArray = api.getJSONArray("api");
        int length = apiArray.length();
        for(int i = 0; i < length; i++) {
            JSONObject action = apiArray.getJSONObject(i);
            String method = action.getString("method");
            RestfulActionWrapper wrapper = compileAction(action);
            List<RestfulActionWrapper> wrapperList = actionMap.get(method);
            if(wrapperList == null) {
                wrapperList = new ArrayList<RestfulActionWrapper>();
                actionMap.put(method, wrapperList);
            }
            wrapperList.add(wrapper);
        }
    }

    private RestfulActionWrapper compileAction(JSONObject action) {
        String url = action.getString("url");
        String actionClazz = action.getString("action");
        String actionID = action.getString("id");

        if(url == null) {
            throw new NullPointerException("url element cannot be empty");
        }

        if(actionClazz == null) {
            throw new NullPointerException("action element cannot be empty");
        }

        if(actionID == null) {
            throw new NullPointerException("action name cannot be empty");
        }

        String[] paths = url.substring(1).split("/");
        List<String> names = new ArrayList<String>();

        StringBuilder patternBuilder = new StringBuilder();

        for (String path : paths) {
            patternBuilder.append("/");
            if (path.startsWith("[")) {
                patternBuilder.append("([\\w\\-\\.~!@\\#$^&*\\=+|;:]*)?");
                String keyName = path.substring(1, path.length()-1).trim();
                names.add(keyName);
            } else {
                patternBuilder.append(path);
            }
        }

        Pattern pattern = Pattern.compile(patternBuilder.toString());
        String[] keyNames = names.toArray(new String[names.size()]);
        try {
            RestfulAction actionInstance = (RestfulAction) Class.forName(actionClazz).newInstance();
            return new RestfulActionWrapper(pattern, keyNames, actionID, actionInstance);
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        return null;
    }

    public RestfulActionInfo getActionInfo(String method, String pathInfo) {
        List<RestfulActionWrapper> wrapperList = actionMap.get(method);
        if(wrapperList != null) {
            for(RestfulActionWrapper wrapper:wrapperList) {
                Pattern pattern = wrapper.getPattern();
                Matcher matcher = pattern.matcher(pathInfo);
                if(matcher.matches()) {
                    return new RestfulActionInfo(wrapper, matcher);
                }
            }
        }
        return null;
    }
}
