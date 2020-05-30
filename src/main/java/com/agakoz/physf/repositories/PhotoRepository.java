package com.agakoz.physf.repositories;

import com.agakoz.physf.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {
}
