package me.seungwoo.controller;

import lombok.RequiredArgsConstructor;
import me.seungwoo.domain.AuthenticationToken;
import me.seungwoo.domain.User;
import me.seungwoo.token.UserKeyProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

/**
 * Created by Leo.
 * User: sonseungwoo
 * Date: 2019-02-20
 * Time: 22:07
 */
@RestController
@RequiredArgsConstructor
public class MainController {

    private final UserKeyProvider tokenProvider;

    private final AuthenticationManager authenticationManager;


    @GetMapping("/token")
    public ResponseEntity<AuthenticationToken> login(@ModelAttribute User user) throws GeneralSecurityException, UnsupportedEncodingException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new AuthenticationToken(jwt));
    }

    @GetMapping("/home")
    public String home(HttpServletRequest request) {
        return "home";
    }

    @GetMapping("/hello")
    public String hello(HttpServletRequest request) {
        return "hello";
    }

    @GetMapping("/my-error-page")
    public String errorPage(){
        return "error";
    }
}
