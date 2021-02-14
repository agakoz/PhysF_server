package com.agakoz.physf.repositories;

import com.agakoz.physf.model.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<UploadedFile, Integer> {

    @Query("SELECT max(id) from UploadedFile ")
    Optional<Integer> getLastId();
}
