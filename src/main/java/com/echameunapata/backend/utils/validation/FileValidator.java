package com.echameunapata.backend.utils.validation;

import com.echameunapata.backend.exceptions.HttpError;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * Utility class for validating file uploads.
 * Enforces restrictions on file types and sizes to ensure security and consistency.
 */
public class FileValidator {

    // Maximum file size: 1 MB (in bytes)
    private static final long MAX_FILE_SIZE = 1024 * 1024; // 1 MB

    // Allowed image MIME types (most popular formats)
    private static final List<String> ALLOWED_MIME_TYPES = Arrays.asList(
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/gif",
            "image/webp"
    );

    // Allowed file extensions
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
            ".jpg",
            ".jpeg",
            ".png",
            ".gif",
            ".webp"
    );

    /**
     * Validates a single file upload.
     * Checks if the file is null, empty, within size limits, and of an allowed type.
     *
     * @param file The MultipartFile to validate
     * @throws HttpError if validation fails
     */
    public static void validateFile(MultipartFile file) {
        // Check if file is null or empty
        if (file == null || file.isEmpty()) {
            throw new HttpError(
                    HttpStatus.BAD_REQUEST,
                    "File is required and cannot be empty"
            );
        }

        // Check file size (must be less than 1 MB)
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new HttpError(
                    HttpStatus.BAD_REQUEST,
                    String.format("File size exceeds the maximum allowed size of 1 MB. Current size: %.2f MB",
                            file.getSize() / (1024.0 * 1024.0))
            );
        }

        // Check content type (MIME type)
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType.toLowerCase())) {
            throw new HttpError(
                    HttpStatus.BAD_REQUEST,
                    String.format("Invalid file type. Allowed types: JPEG, JPG, PNG, GIF, WEBP. Received: %s",
                            contentType != null ? contentType : "unknown")
            );
        }

        // Check file extension
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !hasAllowedExtension(originalFilename)) {
            throw new HttpError(
                    HttpStatus.BAD_REQUEST,
                    String.format("Invalid file extension. Allowed extensions: %s. Received: %s",
                            String.join(", ", ALLOWED_EXTENSIONS),
                            originalFilename != null ? getFileExtension(originalFilename) : "unknown")
            );
        }
    }

    /**
     * Checks if the filename has an allowed extension.
     *
     * @param filename The original filename
     * @return true if the extension is allowed, false otherwise
     */
    private static boolean hasAllowedExtension(String filename) {
        String extension = getFileExtension(filename);
        return ALLOWED_EXTENSIONS.stream()
                .anyMatch(allowed -> allowed.equalsIgnoreCase(extension));
    }

    /**
     * Extracts the file extension from a filename.
     *
     * @param filename The original filename
     * @return The file extension (including the dot), or empty string if none found
     */
    private static String getFileExtension(String filename) {
        if (filename == null) {
            return "";
        }
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return "";
        }
        return filename.substring(lastDotIndex).toLowerCase();
    }

    /**
     * Gets the maximum allowed file size in MB.
     *
     * @return Maximum file size in MB
     */
    public static double getMaxFileSizeMB() {
        return MAX_FILE_SIZE / (1024.0 * 1024.0);
    }

    /**
     * Gets the list of allowed file extensions.
     *
     * @return List of allowed extensions
     */
    public static List<String> getAllowedExtensions() {
        return ALLOWED_EXTENSIONS;
    }

    /**
     * Gets the list of allowed MIME types.
     *
     * @return List of allowed MIME types
     */
    public static List<String> getAllowedMimeTypes() {
        return ALLOWED_MIME_TYPES;
    }
}

