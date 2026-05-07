package com.taskmanager.repository;

import com.taskmanager.entity.TaskHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskHistoryRepository
        extends JpaRepository<TaskHistory, Long> {
}