package me.seungwoo;

import me.seungwoo.util.AES256Util;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringTokenApplicationTests {

    @Test
    public void contextLoads() throws UnsupportedEncodingException, GeneralSecurityException {
        AES256Util aes256 = new AES256Util();
        String str = "skghy3342" + "홍길동|dfsdf|sdfe|2234|01000|ewrew|ewrew|e345ger";
        String token = aes256.encrypt(str);
        System.out.println("token = " + token);
    }

}
