package com.agakoz.physf.repositories;

import com.agakoz.physf.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
}
