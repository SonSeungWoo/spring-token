package me.seungwoo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 비인증 공통 처리
 * <p>
 * Created by Leo.
 * User: ssw
 * Date: 2019-02-12
 * Time: 11:13
 */
@Configuration
public class UnauthorizedAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        String result = String.format("{\n" +
                "    \"isSucceed\": false,\n" +
                "    \"isWarning\": false,\n" +
                "    \"error\": {\n" +
                "        \"message\": \"%s\" \n" +
                "    },\n" +
                "    \"warning\": null,\n" +
                "    \"response\": null\n" +
                "}", HttpStatus.UNAUTHORIZED.value() + " " + e.getMessage());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=utf-8");
        response.getOutputStream().println(result);
    }
}
