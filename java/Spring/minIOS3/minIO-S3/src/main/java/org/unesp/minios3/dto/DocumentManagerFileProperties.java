package org.unesp.minios3.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO para representar as propriedades de um arquivo.
 */
@Data
@Builder
public class DocumentManagerFileProperties {

    private long size;
    private String path;
    private String mimeType;
    private String uuid;
}
