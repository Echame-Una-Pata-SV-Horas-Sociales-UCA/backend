package com.echameunapata.backend.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.echameunapata.backend.services.contract.IFileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class FileStorageServiceImpl implements IFileStorageService {

    private final Cloudinary cloudinary;

    public FileStorageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }


    @Override
    public String uploadFile(MultipartFile file, String folderName) throws IOException {
        // 'echameunapata' actúa como carpeta raíz para mantener orden en tu nube
        String fullPath = "echameunapata/" + folderName;

        // DEFINICIÓN DE OPTIMIZACIONES
        Transformation transformation = new Transformation()
                // 1. LIMITAR TAMAÑO (c_limit)
                // Redimensiona solo si la imagen es mayor a 1280px de ancho.
                // Mantiene la proporción exacta. No distorsiona.
                // 1280px es suficiente para verla nítida en móviles y laptops.
                .width(1280).crop("limit")

                // 2. CALIDAD AUTOMÁTICA (q_auto)
                // El algoritmo reduce el peso del archivo sin que el ojo humano note la diferencia.
                // Reduce el peso un 40-70% automáticamente.
                .quality("auto")

                // 3. FORMATO EFICIENTE (f_auto)
                // Guarda la imagen en el formato más eficiente posible o lo prepara para entrega web.
                // Nota: A veces es mejor forzar 'jpg' o 'webp' aquí si quieres consistencia en BD.
                // Para este caso, 'auto' está bien.
                .fetchFormat("auto");

        // Se sube el archivo convirtiéndolo a bytes.
        // ObjectUtils.asMap permite pasar parámetros opcionales a la API de Cloudinary.
            Map uploadFile = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "folder", fullPath,
                "resource_type", "auto", // Detecta automáticamente si es imagen o video (útil para evidencias)
                "transformation", transformation));

            String publicId = (String) uploadFile.get("public_id");
            return cloudinary.url().secure(true).generate(publicId);

    }

    @Override
    public Map<String, Object> deleteFile(String publicId) throws IOException {
        try {
            // Elimina el archivo usando su public_id
            return (Map<String, Object>) cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new IOException("Error al eliminar el archivo con public_id: " + publicId, e);
        }
    }
}
