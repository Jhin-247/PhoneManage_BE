package com.example.final_version2.utils

import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption


object FileUploadUtil {
    fun saveFile(
        uploadDir: String, fileName: String,
        multipartFile: MultipartFile
    ) {
        val uploadPath: Path = Paths.get(uploadDir)
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath)
        }
        try {
            multipartFile.inputStream.use { inputStream ->
                val filePath: Path = uploadPath.resolve(fileName)
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING)
            }
        } catch (ioe: IOException) {
            throw IOException("Could not save image file: $fileName", ioe)
        }
    }
}