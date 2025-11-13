package com.codegym.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dwjq0zz1q",
                "api_key", "715771774644777",
                "api_secret", "8436Ytfq-sQ_CU1oYjjXbymlK70"));
    }
}