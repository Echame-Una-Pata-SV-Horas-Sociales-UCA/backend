package com.echameunapata.backend.controllers;
import com.echameunapata.backend.domain.dtos.commons.GeneralResponse;
import com.echameunapata.backend.services.contract.IFileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * CONTROLADOR TEMPORAL DE PRUEBAS
 * Este controlador sirve únicamente para verificar la integración con Cloudinary.
 * No debe formar parte de la versión final de producción expuesta al público.
 */
@RestController
@RequestMapping("${api.base-path}/test-media")
public class MediaTestController {

    private final IFileStorageService fileStorageService;

    public MediaTestController(IFileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

//    @PostMapping("/upload")
//    public ResponseEntity<GeneralResponse> uploadTest(@RequestParam("file") MultipartFile file) {
//        try {
//            // Probamos subir a una carpeta llamada "test"
//            Map<String, Object> response = fileStorageService.uploadFile(file, "test_uploads");
//
//            // Retornamos la respuesta completa de Cloudinary para que veas qué datos te devuelve
//            return GeneralResponse.getResponse(
//                    HttpStatus.OK,
//                    "Imagen subida correctamente",
//                    response
//            );
//        } catch (IOException e) {
//            return GeneralResponse.getResponse(
//                    HttpStatus.INTERNAL_SERVER_ERROR,
//                    "Error al subir imagen: " + e.getMessage()
//            );
//        } catch (Exception e) {
//            return GeneralResponse.getResponse(
//                    HttpStatus.INTERNAL_SERVER_ERROR,
//                    "Error inesperado: " + e.getMessage()
//            );
//        }
//    }

    @DeleteMapping("/delete")
    public ResponseEntity<GeneralResponse> deleteTest(@RequestParam("publicId") String publicId) {
        try {
            Map<String, Object> response = fileStorageService.deleteFile(publicId);
            System.out.println(response);

            if (response.get("result").equals("not found")) {
                return GeneralResponse.getResponse(
                        HttpStatus.NOT_FOUND,
                        "No se encontró la imagen con el publicId proporcionado"
                );
            }

            return GeneralResponse.getResponse(
                    HttpStatus.OK,
                    "Imagen eliminada correctamente",
                    response
            );
        } catch (IOException e) {
            return GeneralResponse.getResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al eliminar imagen: " + e.getMessage()
            );
        }
    }
}
