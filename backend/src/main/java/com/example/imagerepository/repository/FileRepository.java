package com.example.imagerepository.repository;

import com.example.imagerepository.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, String> {
    List<FileEntity> findDistinctByNameLikeOrLabels_LabelLike(String name, String label);
}