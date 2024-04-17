package com.study.withus;

import com.study.withus.util.jasypt.JasyptConfig;
import org.assertj.core.api.Assertions;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

class WithusApplicationTests extends JasyptConfig {

    @DisplayName("jasypt를 이용한 패스워드 테스트")
    @Test
    void jasyptConfigTest() {
        //given
        String encryptKey = System.getProperty("jasypt.encryptor.password");
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(encryptKey);
        String password = "password";

        //when
        String encryptedPassword = encryptor.encrypt(password);

        //then
        Assertions.assertThat(password).isEqualTo(encryptor.decrypt(encryptedPassword));
    }

}
