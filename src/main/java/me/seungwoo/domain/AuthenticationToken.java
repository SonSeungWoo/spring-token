package me.seungwoo.domain;

import lombok.Data;

/**
 * Created by Leo.
 * User: ssw
 * Date: 2019-02-18
 * Time: 15:31
 */
@Data
public class AuthenticationToken {
    private String access_token;

    public AuthenticationToken(){

    }

    public AuthenticationToken(String access_token){
        this.access_token = access_token;
    }
}
