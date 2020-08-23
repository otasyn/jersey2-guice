package org.otasyn.template.jersey2.guice;

import javax.servlet.annotation.WebFilter;
import javax.servlet.DispatcherType;

import org.apache.shiro.web.servlet.ShiroFilter;

@WebFilter(
    urlPatterns = "/*",
    dispatcherTypes = {
        DispatcherType.REQUEST,
        DispatcherType.FORWARD,
        DispatcherType.INCLUDE,
        DispatcherType.ERROR
    }
)
public class ProjectShiroFilter extends ShiroFilter { }
