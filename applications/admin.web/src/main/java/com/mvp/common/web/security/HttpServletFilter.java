/**
 * HttpServletFilter.java
 */
package com.mvp.common.web.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is an implementation of {@link Filter} to handle only httpServlet
 * request and response.
 * 
 * @author prakash
 *
 */
public abstract class HttpServletFilter implements Filter {
    /**
     * 
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    /**
     * 
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest iRequest, ServletResponse iResponse, FilterChain iFilterChain)
            throws IOException, ServletException {
        if (!(iRequest instanceof HttpServletRequest) && !(iResponse instanceof HttpServletResponse)) {
            throw new IllegalArgumentException("Only HttpServletRequest & HttpServletResponse is supported ["
                    + iRequest.getClass() + ", " + iResponse.getClass() + "]");
        }
        HttpServletRequest theHttpRequest = (HttpServletRequest) iRequest;
        HttpServletResponse theHttpResponse = (HttpServletResponse) iResponse;
        doFilter(theHttpRequest, theHttpResponse, iFilterChain);
    }

    /**
     * Filter method which passes {@link HttpServletRequest} &
     * {@link HttpServletResponse}
     * 
     * @param iHttpRequest
     * @param iHttpResponse
     * @param iChain
     * @throws IOException
     * @throws ServletException
     */
    public abstract void doFilter(HttpServletRequest iHttpRequest, HttpServletResponse iHttpResponse, FilterChain iChain)
            throws IOException, ServletException;

    /**
     * 
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig iFilterConfig) throws ServletException {

    }

}
