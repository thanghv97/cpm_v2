package tcbs.com.cpm.config;

import feign.codec.ErrorDecoder;
import feign.okhttp.OkHttpClient;
import feign.RetryableException;
import feign.Retryer;
import feign.codec.Encoder;
import feign.form.FormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tcbs.com.cpm.error.CustomErrorDecoder;

@Configuration
@EnableFeignClients(basePackages = {"tcbs.com.cpm.client"})
public class FeignClientConfig {
//
//    @Autowired
//    private ObjectFactory<HttpMessageConverters> messageConverters;
//
//    @Bean
//    public OkHttpClient client() {
//        return new OkHttpClient();
//    }
//
//    @Bean
//    @ConditionalOnMissingBean(value = ErrorDecoder.class)
//    public CustomErrorDecoder errorDecoder() {
//        return new CustomErrorDecoder();
//    }
//
//    @Bean
//    public Encoder feignFormEncoder() {
//        return new FormEncoder(new SpringEncoder(this.messageConverters));
//    }
//
//    @Bean
//    public Retryer retryer() {
//        return new CustomRetryer();
//    }
}

class CustomRetryer implements Retryer {

    private final int maxAttempts;
    private final long backoff;
    int attempt;

    public CustomRetryer() {
        this(2000L, 3);
    }

    public CustomRetryer(long backoff, int maxAttempts) {
        this.backoff = backoff;
        this.maxAttempts = maxAttempts;
        this.attempt = 1;
    }

    @Override
    public void continueOrPropagate(RetryableException e) {
        if (attempt++ >= maxAttempts) {
            throw e;
        }
        try {
            Thread.sleep(backoff);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public Retryer clone() {
        return new CustomRetryer(backoff, maxAttempts);
    }
}
