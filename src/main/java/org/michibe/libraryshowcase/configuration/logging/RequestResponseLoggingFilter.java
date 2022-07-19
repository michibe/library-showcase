package org.michibe.libraryshowcase.configuration.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RequestResponseLoggingFilter extends OncePerRequestFilter implements Ordered {

    private static final Logger REQUEST_LOGGER = LoggerFactory.getLogger("RequestLogger");
    private static final Logger RESPONSE_LOGGER = LoggerFactory.getLogger("ResponseLogger");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final var requestUrl = request.getRequestURI() + request.getQueryString();
        REQUEST_LOGGER.info("Request received", requestUrl);
        try {
            filterChain.doFilter(request, response);
            RESPONSE_LOGGER.info("Response transmitted");
        } catch (Exception e) {
            RESPONSE_LOGGER.info("Response transmitted");
            throw e;
        }

    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 180;
    }
}
