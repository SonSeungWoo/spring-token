package me.seungwoo.token;

import com.authorization.util.AES256Util;
import com.common.dto.UserInfoDto;
import com.common.util.StringUtil;
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

    public String generateToken(Authentication authentication, String key) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        String token = authentication.getName() + ":" + StringUtil.randomUUID();
        String userKey = ase256.aesEncode(token);
        User User = new User();
        User.setUsername(authentication.getName());
        redisTemplate.opsForValue().set(userKey, userInfoDto);
        redisTemplate.expire(userKey, 1, TimeUnit.DAYS);
        return userKey;

    }

    public String getUserIdFromToken(String token) throws Exception {
        User userInfoDto = (User) redisTemplate.opsForValue().get(token);
        return userInfoDto.getUserId();
    }

    public boolean validateToken(String token) {
        try {
            User userInfoDto = (User) redisTemplate.opsForValue().get(token);
            if (userInfoDto.getUserId() != null)
                return true;
        } catch (NullPointerException ex) {
            logger.error("Expired JWT token");
        }
        return false;
    }
}
