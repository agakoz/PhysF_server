package com.agakoz.physf.repositories;

import com.agakoz.physf.model.DTO.PhotoDTO;
import com.agakoz.physf.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {
    @Query("SELECT p.id from Photo p where p.visit.id = :visitId ")
    List<Integer> getPhotoIdsFromVisit(@Param("visitId") int visitId);

    @Query("SELECT new com.agakoz.physf.model.DTO.PhotoDTO(p.id, p.photo) from Photo p where p.visit.id = :visitId ")
    List<PhotoDTO> getPhotosFromVisit(@Param("visitId") int visitId);

    @Query("Select new com.agakoz.physf.model.DTO.PhotoDTO(p.id, p.photo)  from Photo p where p.id in :photoIds")
    List<PhotoDTO> getPhotosListByIds(@Param("photoIds") List<Integer> photoIds);

    @Query("Select new com.agakoz.physf.model.DTO.PhotoDTO(p.id, p.photo)  from Photo p where p.id = :photoId")
    PhotoDTO getPhotoById(@Param("photoId") int photoId);

    @Query("Delete FROM Photo p where p.visit.id = :visitId")
    void deletePhotosFromVisit(@Param("visitId") int visitId);
}
