package com.example.filetest.repository;

import com.example.filetest.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long>, PagingAndSortingRepository<FileEntity, Long> {
}
