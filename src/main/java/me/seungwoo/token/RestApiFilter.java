package me.seungwoo.token;

import me.seungwoo.config.RequestWrapper;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Leo.
 * User: ssw
 * Date: 2019-02-19
 * Time: 08:49
 */
@Configuration
public class RestApiFilter extends GenericFilterBean {

    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
            throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        RequestWrapper requestWrapper = new RequestWrapper((HttpServletRequest) req);
        String body = IOUtils.toString(requestWrapper.getBody(), request.getCharacterEncoding());
        req.setAttribute("requestBody", body);
        chain.doFilter(requestWrapper, res);
    }
}
