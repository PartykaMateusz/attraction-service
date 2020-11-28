package com.mytrip.attractionservice.internal.configuration;

import feign.Request;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties
public class AttractionClientConfiguration {

    @Bean
    public feign.Logger.Level feignLoggerLevel() {
        return feign.Logger.Level.BASIC;
    }

//    @Bean
//    @ConfigurationPropertiesBinding
//    Request.Options options() {
//        return new Request.Options(connectTimeout, readTimeout);
//    }

//    @Bean
//    @ConditionalOnProperty("newgistics-tracking-api.security.oauth2.client-id")
//    public OAuth2FeignRequestInterceptor oauth2RequestInterceptor() {
//        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), oauth2ResourceDetails());
//    }
//
//    @Bean
//    @ConditionalOnProperty("newgistics-tracking-api.security.oauth2.client-id")
//    @ConfigurationProperties("newgistics-tracking-api.security.oauth2")
//    public ClientCredentialsResourceDetails oauth2ResourceDetails() {
//        final ClientCredentialsResourceDetails details = new ClientCredentialsResourceDetails();
//        details.setAccessTokenUri(tokenUri);
//        details.setClientId(clientId);
//        details.setClientSecret(clientSecret);
//        return details;
//    }


}
