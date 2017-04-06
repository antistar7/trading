package net.antistar.restful.context;

import net.antistar.exception.AntistarException;

import javax.servlet.ServletConfig;

/**
 * Created by IntelliJ IDEA.
 * User: syfer
 * Date: 12. 2. 24.
 * Time: 오후 1:36
 * To change this template use File | Settings | File Templates.
 */
public interface RestfulContext {
    public void destroy() throws AntistarException;
    public void init(ServletConfig config) throws AntistarException;
}
