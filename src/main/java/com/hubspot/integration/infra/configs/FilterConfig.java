package com.hubspot.integration.infra.configs;

import com.hubspot.integration.infra.filters.HubspotSignatureFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

@Configuration
public class FilterConfig {

    @Value("${hubspot.client-secret}")
    private String hubspotSecret;

    private String webhookPath = "/webhook/contact";

    @Bean
    public FilterRegistrationBean<HubspotSignatureFilter> hubspotFilter() {
        FilterRegistrationBean<HubspotSignatureFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new HubspotSignatureFilter(hubspotSecret));
        registrationBean.addUrlPatterns("/webhook/contact");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
