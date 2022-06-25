package com.jeffy.encrypt.starter.decrypt;

import com.jeffy.encrypt.starter.anno.Decrypt;
import com.jeffy.encrypt.starter.utils.AESUtils;
import com.jeffy.encrypt.starter.utils.EncryptProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * RequestBodyAdvice 在你使用了 @RequestBody 注解的时候才会生效，
 * 换言之，前后端都是 JSON 交互的时候，这两个才有用。不过一般来说接口加解密的场景也都是前后端分离的时候才可能有的事。
 *
 * 我们没有直接实现 RequestBodyAdvice 接口，而是继承自 RequestBodyAdviceAdapter 类，该类是 RequestBodyAdvice 接口的子类，
 * 并且实现了接口中的一些方法，这样当我们继承自 RequestBodyAdviceAdapter 时，就只需要根据自己实际需求实现某几个方法即可。
 */
@EnableConfigurationProperties(EncryptProperties.class)
@ControllerAdvice
@Slf4j
public class DecryptRequest extends RequestBodyAdviceAdapter {
    @Autowired
    EncryptProperties encryptProperties;

    /**
     * 该方法用来判断哪些接口需要处理接口解密，我们这里的判断逻辑是方法上或者参数上含有 @Decrypt 注解的接口，处理解密问题。
     *
     * @param methodParameter
     * @param targetType
     * @param converterType
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return methodParameter.hasMethodAnnotation(Decrypt.class) || methodParameter.hasParameterAnnotation(Decrypt.class);
    }

    /**
     * 这个方法会在参数转换成具体的对象之前执行，我们先从流中加载到数据，然后对数据进行解密，解密完成后再重新构造 HttpInputMessage 对象返回。
     *
     * @param inputMessage
     * @param parameter
     * @param targetType
     * @param converterType
     * @return
     * @throws IOException
     */
    @Override
    public HttpInputMessage beforeBodyRead(final HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        byte[] body = new byte[inputMessage.getBody().available()];
        inputMessage.getBody().read(body);
        try {
            byte[] decrypt = AESUtils.decrypt(body, encryptProperties.getKey().getBytes());
            final ByteArrayInputStream bais = new ByteArrayInputStream(decrypt);
            return new HttpInputMessage() {
                @Override
                public InputStream getBody() throws IOException {
                    return bais;
                }

                @Override
                public HttpHeaders getHeaders() {
                    return inputMessage.getHeaders();
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.beforeBodyRead(inputMessage, parameter, targetType, converterType);
    }
}