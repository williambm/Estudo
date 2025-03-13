package org.unesp.minios3.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.unesp.minios3.service.ArquivoService;

import java.awt.*;
import java.util.List;

@RestController
@RequestMapping("/arquivos")
public class ArquivoController {

    private final ArquivoService minioService;

    public ArquivoController(ArquivoService minioService) {
        this.minioService = minioService;
    }

    /**
     * Upload de arquivo
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileUrl = minioService.uploadFile(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(fileUrl);
    }

    @GetMapping
    public ResponseEntity<List<String>> listaArquivosDoBucket() {
        //Listar todos os arquivos do bucket, como no service tem o nome do bucket ele traz tudo pelo nome.
        List<String> fileUrls = minioService.listFiles();
        return ResponseEntity.ok(fileUrls);
    }

    /**
     * Dicas:
     *
     * HttpHeaders.CONTENT_DISPOSITION → Define que o conteúdo será um download de arquivo.
     * "attachment; filename=\"" + fileName + "\"" → Formata corretamente o nome do arquivo.
     *
     * A busca por nome de arquivo deve ser realizada com o nome exato!!!
     */
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String fileName, @RequestParam String contentType) {
        Resource arquivoRecuperado = minioService.downloadFile(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(arquivoRecuperado);
    }
}
