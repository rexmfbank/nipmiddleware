package com.globalaccelerex.nipmiddleware.config;

import com.globalaccelerex.nipmiddleware.security.AccessControlHttpClient;
import com.globalaccelerex.nipmiddleware.security.AccessControlRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
@Configuration
public class BeanConfig {

    @Bean("threadPoolTaskExecutor")
    public TaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(1000);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("Async-");
        return executor;
    }

    @Bean
    public AccessControlHttpClient getAccessControlHttpClient(AccessControlConfig config) {
        log.trace(" setting HTTP client for access control service");
        return new AccessControlHttpClient.HTTPClientBuilder(config)
                .restTemplate(new AccessControlRestTemplate().getClient())
                .createClient();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
