package me.seungwoo.token;

import me.seungwoo.config.RequestWrapper;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${obt.applicationId}")
    private String applicationId;

    @Value("${obt.restApiToken}")
    private String restApiToken;

    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
            throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        RequestWrapper requestWrapper = new RequestWrapper((HttpServletRequest) req);
        String body = IOUtils.toString(requestWrapper.getBody(), request.getCharacterEncoding());
        if (body.equals("")) {
            throw new ServletException("Missing or invalid Authorization body apiKey");
        }
        JSONObject obj = new JSONObject(body);
        String authId = obj.get("authID").toString();
        String authPassword = obj.get("authPassword").toString();

        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);

            chain.doFilter(requestWrapper, res);
        } else {
            if (authId == null || authPassword == null) {
                throw new ServletException("Missing or invalid Authorization header apiKey");
            }
            if (restApiToken.equals(authPassword) && applicationId.equals(authId)) {
                req.setAttribute("body", body);
            } else {
                throw new ServletException("Invalid authID, authPassword");
            }
            chain.doFilter(requestWrapper, res);
        }
    }
}
