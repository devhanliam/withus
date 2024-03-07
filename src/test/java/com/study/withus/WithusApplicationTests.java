package com.study.withus;

import com.study.withus.util.jasypt.JasyptConfig;
import org.assertj.core.api.Assertions;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

//@SpringBootTest
class WithusApplicationTests extends JasyptConfig {

    @Test
    void jasyptConfigTest() {
        String encryptKey = System.getProperty("jasypt.encryptor.password");
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(encryptKey);
    }

}
