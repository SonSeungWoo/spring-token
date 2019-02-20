package me.seungwoo.token;

import me.seungwoo.domain.User;
import me.seungwoo.util.AES256Util;
import me.seungwoo.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Leo.
 * User: ssw
 * Date: 2019-02-20
 * Time: 14:22
 */
@Configuration
public class UserKeyProvider {

    private static final Logger logger = LoggerFactory.getLogger(UserKeyProvider.class);

    @Resource(name = "redisToken")
    private RedisTemplate<String, Object> redisTemplate;

    private static final AES256Util ase256 = new AES256Util();

    public String generateToken(Authentication authentication) throws GeneralSecurityException, UnsupportedEncodingException {
        String token = authentication.getName() + ":" + StringUtil.randomUUID();
        String userKey = ase256.encrypt(token);
        User user = new User();
        user.setUsername(authentication.getName());
        redisTemplate.opsForValue().set(userKey, user);
        redisTemplate.expire(userKey, 1, TimeUnit.DAYS);
        return userKey;

    }

    public String getUserIdFromToken(String token) throws Exception {
        User user = (User) redisTemplate.opsForValue().get(token);
        return user.getUsername();
    }

    public boolean validateToken(String token) {
        try {
            User user = (User) redisTemplate.opsForValue().get(token);
            if (user.getUsername() != null)
                return true;
        } catch (NullPointerException ex) {
            logger.error("Expired JWT token");
        }
        return false;
    }
}
