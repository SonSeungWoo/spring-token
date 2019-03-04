package me.seungwoo.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Leo.
 * User: ssw
 * Date: 2019-02-21
 * Time: 16:25
 */
@Slf4j
@Configuration
public class ApiInterceptor implements HandlerInterceptor {

    @Value("${rest.application.id}")
    private String applicationId;

    @Value("${rest.api.token}")
    private String restApiToken;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String body = String.valueOf(request.getAttribute("requestBody"));
        JSONObject obj = new JSONObject(body);
        String authId = obj.get("authID").toString();
        String authPassword = obj.get("authPassword").toString();
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            if (authId == null || authPassword == null) {
                throw new ServletException("Missing or invalid Authorization header apiKey");
            }
            if (!restApiToken.equals(authPassword) || !applicationId.equals(authId)) {
                throw new ServletException("Invalid authID, authPassword");
            }
        }
        return true;
    }
}
