package org.otasyn.template.jersey2.guice;

import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.DispatcherType;

import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;

@WebFilter(
    urlPatterns = "/*",
    dispatcherTypes = {
        DispatcherType.REQUEST,
        DispatcherType.FORWARD
    },
    initParams = {
        @WebInitParam(name = "confReloadCheckInterval", value = "-1"),
        @WebInitParam(name = "confPath", value = "/WEB-INF/urlrewrite.xml"),
        @WebInitParam(name = "logLevel", value = "slf4j"),
        @WebInitParam(name = "statusPath", value = "/rewrite-status"),
        @WebInitParam(name = "statusEnabled", value = "true"),
        @WebInitParam(name = "statusEnabledOnHosts", value = "localhost, local, 127.0.0.1"),
        @WebInitParam(name = "modRewriteConf", value = "false"),
        @WebInitParam(name = "modRewriteConfText", value = ""),
        @WebInitParam(name = "allowConfSwapViaHttp", value = "false")
    }
)
public class ProjectUrlRewriteFilter extends UrlRewriteFilter { }
