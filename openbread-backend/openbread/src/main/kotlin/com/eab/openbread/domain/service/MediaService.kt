package com.eab.openbread.domain.service

import com.eab.openbread.domain.ImageProfile
import net.coobird.thumbnailator.Thumbnails
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayOutputStream

@Service
class MediaService(private val fileService: FileService) {

    fun processAndSaveImage(file: MultipartFile, profile: ImageProfile): String {
        if (file.isEmpty) throw IllegalArgumentException("error.file.empty")

        val outputStream = ByteArrayOutputStream()
        Thumbnails.of(file.inputStream)
            .size(profile.maxWidth, profile.maxHeight)
            .outputQuality(profile.quality)
            .toOutputStream(outputStream)

        val processedBytes = outputStream.toByteArray()

        return fileService.saveFileFromBytes(
            processedBytes,
            file.originalFilename ?: "image.jpg",
            profile.folderName
        )
    }

    fun deleteMedia(path: String?) {
        path?.let { fileService.deleteFile(it) }
    }
}