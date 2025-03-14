package org.unesp.minios3.controller;

import org.springframework.core.io.Resource;
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
import org.springframework.web.server.ResponseStatusException;
import org.unesp.minios3.dto.DocumentManagerFileProperties;
import org.unesp.minios3.service.ArquivoService;

import java.util.List;
import java.util.UUID;

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
     * <p>
     * HttpHeaders.CONTENT_DISPOSITION → Define que o conteúdo será um download de arquivo.
     * "attachment; filename=\"" + fileName + "\"" → Formata corretamente o nome do arquivo.
     * <p>
     * A busca por nome de arquivo deve ser realizada com o nome exato!!!
     */
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(
            @RequestParam String fileName
//            @RequestParam String contentType
    ) {
        Resource arquivoRecuperado = minioService.downloadFile(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .contentType(MediaType.parseMediaType(contentType)) //Forma de passar o tipo do arquivo de maneira dinâmica, podendo ser via requestParam, ou pelas propriedades do arquivo.
                .body(arquivoRecuperado);
    }

    /**
     * Meus arquivos são salvos com UUID, então as operações devem ser feita por UUID.
     */
    @GetMapping("/propriedades")
    public ResponseEntity<DocumentManagerFileProperties> getArquivoPropriedades(@RequestParam String fileName) {
        try {
            UUID uuid = UUID.fromString(fileName);
            return ResponseEntity.ok(minioService.getArquivoPropriedades(uuid));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "UUID inválido");
        }
    }
}
