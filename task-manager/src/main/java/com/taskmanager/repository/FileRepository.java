package com.taskmanager.repository;

import com.taskmanager.entity.FileAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileAttachment, Long> {
}