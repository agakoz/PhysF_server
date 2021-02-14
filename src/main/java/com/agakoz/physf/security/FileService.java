package com.agakoz.physf.security;

import com.agakoz.physf.model.UploadedFile;
import com.agakoz.physf.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
public class FileService {
    final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public int saveFile(MultipartFile file) {
        UploadedFile fileToSave = new UploadedFile();
        String originalFileName = file.getOriginalFilename();
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(originalFileName));
        String[] fileNameType = fileName.split("\\.");
        fileToSave.setName(fileNameType[0]);
        fileToSave.setType(fileNameType[fileNameType.length - 1]);
        try {
            fileToSave.setData(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int newFileId = getNewId();
        fileToSave.setId(newFileId);
        fileRepository.save(fileToSave);
        return newFileId;
    }

    private int getNewId() {
        Optional<Integer> idOpt = fileRepository.getLastId() ;
        return idOpt.map(integer -> integer + 1).orElse(0);
    }
}
