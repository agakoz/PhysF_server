package com.agakoz.physf.repositories;

import com.agakoz.physf.model.DTO.FileDTO;
import com.agakoz.physf.model.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<UploadedFile, Integer> {

    @Query("SELECT max(id) from UploadedFile ")
    Optional<Integer> getLastId();

    @Query("SELECT new com.agakoz.physf.model.DTO.FileDTO(f.name, f.type, f.data) FROM UploadedFile f where f.id = :fileId")
    Optional<FileDTO> retrieveFileDTOById(int fileId);

    @Query("SELECT f.name FROM UploadedFile f where f.id = :fileId")
    String getFileName(int fileId);

    @Query("SELECT f.type FROM UploadedFile f where f.id = :fileId")
    String getFileType(int fileId);

}
