package org.unesp.minios3.service;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ArquivoService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    public ArquivoService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }
}
