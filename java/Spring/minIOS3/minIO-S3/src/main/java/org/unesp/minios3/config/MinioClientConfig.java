package org.unesp.minios3.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioClientConfig {

    @Value("${minio.url}")
    private String endpoint;
    @Value("${minio.user}")
    private String username;
    @Value("${minio.password}")
    private String password;

    @Bean
    MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(username, password)
                .build();
    }

}
