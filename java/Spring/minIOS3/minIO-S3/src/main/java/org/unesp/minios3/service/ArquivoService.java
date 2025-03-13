package org.unesp.minios3.service;

import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ArquivoService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    public ArquivoService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    /**
     * Upload de arquivo para o MinIO.
     * <p>
     * Para fazer o upload de um arquivo para o MinIO, é necessário criar um objeto PutObjectArgs. Esse objeto já possui um builder
     * que auxilia nessa criação do upload.
     */
    public String uploadFile(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            //Mapa Opcional de metadados - Nomes devem ser sem espaço e minúsculos aparentemente
            Map<String, String> metadata = Map.of(
                    "nome_original", file.getOriginalFilename(),
                    "tag_sisproec", "Bolsista"
            );

            //Try With Resources - aqui ele vai fechar o InputStream automaticamente, mesmo que ocorra uma exceção.
            try (InputStream inputStream = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(fileName)
                                .stream(inputStream, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .userMetadata(metadata) //Opcionalmente, podemos passar metadados para o arquivo
                                .build()
                );
            }

            //Aqui estou retornando o caminho completo do arquivo, incluindo o nome do bucket.Mas poderia ser um DTO personalizado
            return "/" + bucketName + "/" + fileName;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao fazer upload para MinIO: " + e.getMessage(), e);
        }
    }

    /**
     * Lista todos os arquivos no bucket.
     * Documentação: https://min.io/docs/minio/linux/developers/java/API.html#listObjects
     */
    public List<String> listFiles() {
        try {
            // A listagem dos arquivos do Bucket Retorna um Iterable<Result<Item>> Result e Item são do Minio
            Iterable<Result<Item>> resultados = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .build()
            );
            List<String> nomesArquivos = new ArrayList<>();
            // Iterando sobre os resultados e adicionando os nomes dos arquivos à lista, mas poderia ser um DTO personalizado com mais informações
            resultados.forEach(itemResult -> {
                try {
                    nomesArquivos.add(itemResult.get().objectName());
                } catch (ErrorResponseException |
                         InsufficientDataException |
                         InternalException |
                         InvalidKeyException |
                         InvalidResponseException |
                         IOException |
                         NoSuchAlgorithmException |
                         ServerException |
                         XmlParserException e) {
                    throw new RuntimeException(e.getMessage());
                }
            });

            return nomesArquivos;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar arquivos: " + e.getMessage(), e);
        }
    }

    public Resource downloadFile(String fileName) {
        //InputStream não pode ser usado em um try with Resources, senão ele é fechado antes de ser usado!
        try {
            InputStream stream = minioClient.getObject(
                    io.minio.GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build());
            //Converte o InputStream em um Resource, que é o tipo que o Spring Boot entende como um recurso.
            return new InputStreamResource(stream);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Arquivo não encontrado: " + fileName);
        }
    }
}
