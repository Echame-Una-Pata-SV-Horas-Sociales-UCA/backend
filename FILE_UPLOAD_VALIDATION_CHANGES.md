# File Upload Validation Changes

## Summary
This document outlines all changes made to implement file upload validation across the application. The implementation ensures that:
1. Only image files with popular formats are accepted (JPEG, JPG, PNG, GIF, WEBP)
2. Each request is limited to a single file
3. File size is limited to 1 MB maximum
4. Appropriate error messages are returned when validation fails

---

## Changes Made

### 1. New File: FileValidator Utility Class
**Location:** `/src/main/java/com/echameunapata/backend/utils/validation/FileValidator.java`

A new utility class that provides centralized file validation logic:
- **Validates file existence**: Checks if file is null or empty
- **Validates file size**: Ensures file size is less than 1 MB
- **Validates MIME type**: Accepts only `image/jpeg`, `image/jpg`, `image/png`, `image/gif`, `image/webp`
- **Validates file extension**: Checks for `.jpg`, `.jpeg`, `.png`, `.gif`, `.webp` extensions
- **Provides clear error messages**: Returns descriptive error messages when validation fails

**Key Methods:**
- `validateFile(MultipartFile file)` - Main validation method
- `getMaxFileSizeMB()` - Returns max file size (1 MB)
- `getAllowedExtensions()` - Returns list of allowed extensions
- `getAllowedMimeTypes()` - Returns list of allowed MIME types

---

### 2. Updated: FileStorageServiceImpl
**Location:** `/src/main/java/com/echameunapata/backend/services/impl/FileStorageServiceImpl.java`

**Changes:**
- Added import: `com.echameunapata.backend.utils.validation.FileValidator`
- Added validation call at the beginning of `uploadFile()` method:
  ```java
  // Validate file before uploading
  FileValidator.validateFile(file);
  ```

**Impact:** All file uploads through this service are now automatically validated before being sent to Cloudinary.

---

### 3. Updated: CreateEvidenceDto
**Location:** `/src/main/java/com/echameunapata/backend/domain/dtos/reportEvidence/CreateEvidenceDto.java`

**Changes:**
- Changed from array to single file:
  - Before: `private MultipartFile[] images;`
  - After: `private MultipartFile image;`
- Added validation message: `@NotNull(message = "Image file is required")`
- Removed unused imports

**Impact:** Evidence creation endpoints now accept only 1 image per request instead of multiple images.

---

### 4. Updated: CreateReportDto
**Location:** `/src/main/java/com/echameunapata/backend/domain/dtos/reports/CreateReportDto.java`

**Changes:**
- Enhanced validation message on `photo` field:
  ```java
  @NotNull(message = "Photo is required. Allowed formats: JPEG, PNG, GIF, WEBP. Maximum size: 1 MB")
  private MultipartFile photo;
  ```
- Removed unused imports

**Impact:** Better user feedback when creating reports with invalid files.

---

### 5. Updated: ImageUploadRequestDTO
**Location:** `/src/main/java/com/echameunapata/backend/domain/dtos/image/ImageUploadRequestDTO.java`

**Changes:**
- Enhanced validation message on `file` field:
  ```java
  @NotNull(message = "File is required. Allowed formats: JPEG, PNG, GIF, WEBP. Maximum size: 1 MB")
  private MultipartFile file;
  ```

**Impact:** Better user feedback for generic image upload endpoints.

---

### 6. Updated: GlobalErrorHandler
**Location:** `/src/main/java/com/echameunapata/backend/handlers/GlobalErrorHandler.java`

**Changes:**
- Added imports:
  - `org.springframework.web.multipart.MaxUploadSizeExceededException`
  - `org.springframework.web.multipart.MultipartException`

- Added new exception handlers:
  ```java
  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<GeneralResponse> maxUploadSizeExceededHandler(...)
  
  @ExceptionHandler(MultipartException.class)
  public ResponseEntity<GeneralResponse> multipartExceptionHandler(...)
  ```

**Impact:** Catches file size exceptions at the Spring Boot level (before reaching application code) and returns user-friendly error messages.

---

### 7. Updated: application.properties
**Location:** `/src/main/resources/application.properties`

**Changes:**
- Added file upload configuration:
  ```properties
  # FILE UPLOAD CONFIGURATION
  # Maximum file size for a single file upload
  spring.servlet.multipart.max-file-size=1MB
  # Maximum request size (for multipart requests with multiple parts)
  spring.servlet.multipart.max-request-size=1MB
  ```

**Impact:** Spring Boot now enforces the 1 MB file size limit at the container level, rejecting oversized files before they reach application code.

---

## Affected Endpoints

The following endpoints now have file validation:

### 1. Report Endpoints
- **POST** `/api/v1/reports/create`
  - Accepts: Single image file
  - Validates: File type, size, extension

### 2. Animal Endpoints
- **POST** `/api/v1/animal/register`
  - Accepts: Single image file (optional)
  - Validates: File type, size, extension

- **PUT** `/api/v1/animal/update/{id}`
  - Accepts: Single image file (optional)
  - Validates: File type, size, extension

### 3. Any Future Endpoints Using FileStorageService
All endpoints that use the `FileStorageServiceImpl.uploadFile()` method will automatically benefit from this validation.

---

## Validation Rules

### Accepted File Formats
- JPEG (`.jpg`, `.jpeg`)
- PNG (`.png`)
- GIF (`.gif`)
- WEBP (`.webp`)

### File Size Limit
- Maximum: **1 MB** (1,048,576 bytes)

### Files Per Request
- Maximum: **1 file per request**

---

## Error Responses

### File Too Large
```json
{
  "status": 400,
  "message": "File size exceeds the maximum allowed size of 1 MB. Current size: X.XX MB"
}
```

### Invalid File Type (MIME Type)
```json
{
  "status": 400,
  "message": "Invalid file type. Allowed types: JPEG, JPG, PNG, GIF, WEBP. Received: application/pdf"
}
```

### Invalid File Extension
```json
{
  "status": 400,
  "message": "Invalid file extension. Allowed extensions: .jpg, .jpeg, .png, .gif, .webp. Received: .pdf"
}
```

### File Missing
```json
{
  "status": 400,
  "message": "File is required and cannot be empty"
}
```

---

## Testing Recommendations

### Test Cases to Verify

1. **Valid File Upload**
   - Upload a JPEG file < 1 MB
   - Should succeed

2. **File Too Large**
   - Upload a file > 1 MB
   - Should return 400 error with size message

3. **Invalid File Type**
   - Upload a PDF, DOCX, or other non-image file
   - Should return 400 error with type message

4. **Invalid Extension**
   - Upload a file with renamed extension (e.g., image.txt)
   - Should return 400 error

5. **Missing File**
   - Send request without file
   - Should return 400 error

6. **Multiple Endpoints**
   - Test all affected endpoints to ensure consistency

---

## Benefits

1. **Security**: Prevents upload of potentially malicious files
2. **Storage Optimization**: Limits file sizes to prevent storage bloat
3. **User Experience**: Provides clear, actionable error messages
4. **Consistency**: Centralized validation ensures uniform behavior across all endpoints
5. **Performance**: Rejects invalid files early, before cloud storage operations
6. **Cost Control**: Reduces unnecessary uploads to Cloudinary

---

## Future Enhancements (Optional)

1. Add image dimension validation (e.g., max 4000x4000 pixels)
2. Add virus scanning for uploaded files
3. Add rate limiting for file uploads per user
4. Add support for additional formats if needed (e.g., SVG, BMP)
5. Add file content validation (not just extension checking)
6. Add upload progress tracking for large files
7. Add thumbnail generation for uploaded images

---

## Maintenance Notes

- If you need to change the max file size, update:
  1. `FileValidator.MAX_FILE_SIZE` constant
  2. `application.properties` (both max-file-size and max-request-size)
  
- If you need to add new allowed formats:
  1. Add MIME type to `FileValidator.ALLOWED_MIME_TYPES`
  2. Add extension to `FileValidator.ALLOWED_EXTENSIONS`
  3. Update error messages in `FileValidator`
  4. Update documentation

---

## Build Status

✅ **Build Successful** - All changes compiled without errors.

The project builds successfully with these changes. Only warnings present are pre-existing (deprecated API usage in GoogleCalendarServiceImpl and unchecked operations in FileStorageServiceImpl from the Cloudinary library).

