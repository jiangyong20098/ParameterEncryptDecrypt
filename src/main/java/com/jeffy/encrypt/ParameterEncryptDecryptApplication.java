package com.jeffy.encrypt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 参数/响应加解密的思路。
 * ResponseBodyAdvice 和 RequestBodyAdvice 的用法。
 */
@SpringBootApplication
public class ParameterEncryptDecryptApplication {
    public static void main(String[] args) {
        SpringApplication.run(ParameterEncryptDecryptApplication.class, args);
    }
}
