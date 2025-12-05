package com.echameunapata.backend.services.contract;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface IFileStorageService {
    /**
     * Sube un archivo al proveedor de almacenamiento externo.
     *
     * @param file El archivo recibido del cliente (MultipartFile).
     * @param folderName El nombre de la carpeta organizativa (ej: "animals", "evidences").
     * @return Un Mapa con la respuesta cruda del proveedor (url, public_id, etc.).
     * @throws IOException Si ocurre un error de entrada/salida durante la subida.
     */
    String uploadFile(MultipartFile file, String folderName) throws IOException;

    /**
     * Elimina un archivo del proveedor de almacenamiento.
     *
     * @param publicId El identificador único del archivo en el proveedor.
     * @return Un Mapa con la respuesta cruda del proveedor sobre la eliminación.
     * @throws IOException Si ocurre un error de comunicación con el proveedor.
     */
    Map<String, Object> deleteFile(String publicId) throws IOException;
}
