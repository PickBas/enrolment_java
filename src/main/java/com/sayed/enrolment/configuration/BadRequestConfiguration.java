package com.sayed.enrolment.configuration;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Configuration
public class BadRequestConfiguration {

    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
                Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
                errorAttributes.put("code", errorAttributes.get("status"));
                errorAttributes.put("message", "Validation Failed");
                errorAttributes.remove("path");
                errorAttributes.remove("status");
                errorAttributes.remove("timestamp");
                errorAttributes.remove("error");
                return errorAttributes;
            }
        };
    }
}
