package com.example.final_version2.controller

import org.springframework.core.io.ByteArrayResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.nio.file.Files
import java.nio.file.Paths


@RestController
@RequestMapping("api/image")
class ImageController {

    @GetMapping("get_user_image/{email}")
    fun getUserAvatar(@PathVariable("email") email: String): ResponseEntity<ByteArrayResource?>? {
        var actualPhotoPath = email
        if (actualPhotoPath.contains('@')) {
            actualPhotoPath = actualPhotoPath.substring(0, actualPhotoPath.indexOf('@'))
        }
        val fileName = Paths.get("user-photos", "$actualPhotoPath.jpg")
        val buffer = Files.readAllBytes(fileName)
        val byteArrayResource = ByteArrayResource(buffer)
        return ResponseEntity.ok()
            .contentLength(buffer.size.toLong())
            .contentType(MediaType.IMAGE_PNG)
            .body<ByteArrayResource>(byteArrayResource)
    }

    @GetMapping("get_app_icon/{path}")
    fun getAppIcon(@PathVariable("path") path: String): ResponseEntity<ByteArrayResource?>? {
        val fileName = Paths.get("app-icons", "$path.jpg")
        val buffer = Files.readAllBytes(fileName)
        val byteArrayResource = ByteArrayResource(buffer)
        return ResponseEntity.ok()
            .contentLength(buffer.size.toLong())
            .contentType(MediaType.IMAGE_PNG)
            .body<ByteArrayResource>(byteArrayResource)
    }

//    @GetMapping("get_user_image/{email}")
//    @ResponseBody
//    fun getImageDynamicType(
//        @PathVariable("email") email: String,
//        @RequestParam("jpg") jpg: Boolean
//    ): ResponseEntity<InputStreamResource?>? {
//        var actualPhotoPath = email
//        if (actualPhotoPath.contains('@')) {
//            actualPhotoPath = actualPhotoPath.substring(0, actualPhotoPath.indexOf('@'))
//        }
//        val contentType = if (jpg) MediaType.IMAGE_JPEG else MediaType.IMAGE_PNG
//        val fileName = Paths.get("user-photos", "$actualPhotoPath.jpg")
//        val inStream = javaClass.getResourceAsStream(fileName.toAbsolutePath().toString())
//        print(fileName.toString())
//        return ResponseEntity.ok()
//            .contentType(contentType)
//            .body<InputStreamResource?>(inStream?.let { InputStreamResource(it) })
//    }

}