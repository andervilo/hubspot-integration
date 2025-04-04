package com.hubspot.integration.infra.configs;

import com.hubspot.integration.infra.filters.HubspotSignatureFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

@Configuration
public class FilterConfig {

    public static final String WEBHOOK_CONTACT = "/webhook/contact";
    @Value("${hubspot.client-secret}")
    private String hubspotSecret;


    @Bean
    public FilterRegistrationBean<HubspotSignatureFilter> hubspotFilter() {
        FilterRegistrationBean<HubspotSignatureFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new HubspotSignatureFilter(hubspotSecret));
        registrationBean.addUrlPatterns(WEBHOOK_CONTACT);
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
