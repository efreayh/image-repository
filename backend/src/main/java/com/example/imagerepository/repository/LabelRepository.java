package com.example.imagerepository.repository;

import com.example.imagerepository.model.LabelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelRepository extends JpaRepository<LabelEntity, Long> {
}