package com.hubspot.integration.infra.configs;

import feign.Logger;
import org.springframework.context.annotation.Bean;

public class HubspotFeignConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
