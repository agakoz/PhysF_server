package com.agakoz.physf.controllers;

import com.agakoz.physf.security.FileService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/file")
public class FileController {
    final
    FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    @SneakyThrows
    @ResponseBody
    public int uploadFile(@RequestParam("file") MultipartFile file) {
        int fileId = this.fileService.saveFile(file);
        return fileId;
    }
}
