package com.keya.flexoffice.config;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@AllArgsConstructor
public class ConnectorFilter implements Filter {
    private static final String USER_IP="userIp";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

                String userIp = httpRequest.getHeader(USER_IP);
                if (StringUtils.isNotBlank(userIp)){
                    ConnectorManager.builder().userIp(userIp).build();
                }

    }
}
