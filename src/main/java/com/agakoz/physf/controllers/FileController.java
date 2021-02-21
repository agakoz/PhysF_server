package com.agakoz.physf.controllers;

import com.agakoz.physf.model.DTO.FileDTO;
import com.agakoz.physf.model.UploadedFile;
import com.agakoz.physf.repositories.FileRepository;
import com.agakoz.physf.security.FileService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/file")
public class FileController {
    final
    FileService fileService;
    FileRepository fileRepository;

    @Autowired
    public FileController(FileService fileService, FileRepository fileRepository) {
        this.fileService = fileService;
        this.fileRepository = fileRepository;
    }

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    @SneakyThrows
    @ResponseBody
    public int uploadFile(@RequestParam("file") MultipartFile file) {
        int fileId = this.fileService.saveFile(file);
        return fileId;
    }

    //https://www.devglan.com/spring-boot/spring-boot-file-upload-download
    @GetMapping("/download/{fileId}")
    @ResponseBody
    @SneakyThrows
    public ResponseEntity<byte[]> downloadFromDB(@PathVariable int fileId) {
        FileDTO file = fileService.prepareFile(fileId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "." + file.getType() + "\""+"; filetype=\"" + file.getType() + "\"")
                .body(file.getData());
    }
}
