package me.seungwoo.token;

import me.seungwoo.config.RequestWrapper;
import me.seungwoo.service.UserService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Leo.
 * User: ssw
 * Date: 2019-02-18
 * Time: 13:23
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserKeyProvider userKeyProvider;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        if ("OPTIONS".equals(req.getMethod())) {
            res.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(req, res);
        } else {
            try {
                String jwt = getJwtFromRequest(req);
                if (StringUtils.hasText(jwt) && userKeyProvider.validateToken(jwt)) {
                    String userId = userKeyProvider.getUserIdFromToken(jwt);
                    UserDetails ckUserDetails = userService.loadUserByUsername(userId);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(ckUserDetails.getUsername(), ckUserDetails.getPassword(), ckUserDetails.getAuthorities());
                    SecurityContext securityContext = SecurityContextHolder.getContext();
                    securityContext.setAuthentication(authentication);
                }
            } catch (Exception ex) {
                logger.error("Could not set user authentication in security context", ex);
            }
        }

        filterChain.doFilter(req, res);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String token = request.getHeader("access_token");
        if (StringUtils.hasText(token)) {
            return token;
        }
        return null;
    }
}
