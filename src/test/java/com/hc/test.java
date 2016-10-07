package com.hc;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.nio.charset.Charset;

/**
 * Created by hexi on 16-10-5.
 */
public class test {
    private static final String salt = "salt";
    private String defaultS = "3fb2941334f6e9bb498ed1243b55285c";
    @Test
    public void t() {
//        String encode = new Md5Hash("123456",Md5Hash.ALGORITHM_NAME).toHex();
//        System.out.println(encode);
//        System.out.println(encode.length());
//        System.out.println(encode.equals(defaultS));
        String s = (String) null;
        System.out.println(s);

        String s1 = new Md5Hash(null, Md5Hash.ALGORITHM_NAME).toHex();
        System.out.println(s1);
    }
}
