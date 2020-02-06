package com.example.orderMeal.config;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class AccessControlAllowOriginFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        chain.doFilter(req, response);
    }

    public void init(FilterConfig filterConfig) {

    }

    public void destroy() {

    }

    @Override
    public boolean isLoggable(LogRecord record) {
        return false;
    }
}