package com.eab.openbread.domain.service

import com.eab.openbread.domain.exception.ResourceNotFoundException
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.UUID
import kotlin.math.log

@Service
class FileService {
    private val rootLocation = Paths.get("uploads")

    init {
        if (!Files.exists(rootLocation)) {
            Files.createDirectories(rootLocation)
        }
    }

    fun saveFile(file: MultipartFile, entityFolder: String): String {
        if (file.isEmpty) {
            throw ResourceNotFoundException("error.file.empty")
        }

        val contentType = file.contentType ?: ""
        val allowedTypes = listOf("image/jpeg", "image/png", "application/pdf")
        if (!allowedTypes.contains(contentType)) {
            throw IllegalArgumentException("error.file.type_not_allowed")
        }

        try {
            val targetDirectory = rootLocation.resolve(entityFolder)
            if (!Files.exists(targetDirectory)) {
                Files.createDirectories(targetDirectory)
            }

            val originalExtension = file.originalFilename?.substringAfterLast(".", "") ?: ""
            val uniqueFileName = "${UUID.randomUUID()}.$originalExtension"

            val destinationFile = targetDirectory.resolve(Paths.get(uniqueFileName))
                .normalize()
                .toAbsolutePath()

            file.inputStream.use { inputStream ->
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING)
            }

            return "uploads/$entityFolder/$uniqueFileName"
        } catch (e: IOException) {
            throw RuntimeException("error.file.save_failed")
        }

    }

    fun saveFileFromBytes(bytes: ByteArray, originalFilename: String, entityFolder: String): String {
        try {
            val targetDirectory = rootLocation.resolve(entityFolder)
            if (!Files.exists(targetDirectory)) {
                Files.createDirectories(targetDirectory)
            }

            val originalExtension = originalFilename.substringAfterLast(".", "jpg")
            val uniqueFileName = "${UUID.randomUUID()}.$originalExtension"
            val destinationFile = targetDirectory.resolve(uniqueFileName).normalize().toAbsolutePath()

            Files.write(destinationFile, bytes)

            return "uploads/$entityFolder/$uniqueFileName"
        } catch (e: IOException) {
            throw RuntimeException("error.file.save_failed")
        }
    }

    fun deleteFile(relativePath: String) {
        try {
            val fullPath = Paths.get(relativePath).normalize().toAbsolutePath()
            if (fullPath.toString().contains("uploads")) {
                Files.deleteIfExists(fullPath)
            }
        } catch (e: IOException) { }
    }

}