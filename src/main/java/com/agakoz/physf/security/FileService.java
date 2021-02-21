package com.agakoz.physf.security;

import com.agakoz.physf.model.DTO.FileDTO;
import com.agakoz.physf.model.UploadedFile;
import com.agakoz.physf.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
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
        Optional<Integer> idOpt = fileRepository.getLastId();
        return idOpt.map(integer -> integer + 1).orElse(0);
    }

    public FileDTO prepareFile(int fileId) throws NoSuchFileException {
        Optional<FileDTO> fileOpt = fileRepository.retrieveFileDTOById(fileId);
        if (fileOpt.isEmpty())
            throw new NoSuchFileException(String.valueOf(fileId));
        else {
            FileDTO file = fileOpt.get();
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; file-name=" + file.getName() + "; file-type=" + file.getType() + "\"");
            responseHeaders.add("file-type", file.getType());
            responseHeaders.add("file-name", file.getType());
            return file;
        }

    }

    public String getFileNameWithType(int fileId) {
        String fullName= this.fileRepository.getFileName(fileId) + "." + this.fileRepository.getFileType(fileId) ;
        return fullName;
    }

    public void deleteFile(UploadedFile fileToDelete) {
        this.fileRepository.deleteById(fileToDelete.getId());
    }
}
