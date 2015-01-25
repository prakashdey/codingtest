/**
 * SecurityFilter.java
 */
package com.mvp.admin.web.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvp.common.web.security.HttpServletFilter;

/**
 * This class will be used to Secure and intercept all access. <br/>
 * TODO: Need to use proper security protocol.
 * 
 * @author prakash
 *
 */
public class SecurityFilter extends HttpServletFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityFilter.class);

    @Override
    public void doFilter(HttpServletRequest iHttpRequest, HttpServletResponse iHttpResponse, FilterChain iChain)
            throws IOException, ServletException {

        String url = iHttpRequest.getRequestURI();
        LOGGER.debug("Requested URI: " + url);

        iChain.doFilter(iHttpRequest, iHttpResponse);
    }

}
