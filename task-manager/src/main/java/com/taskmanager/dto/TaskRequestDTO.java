package com.taskmanager.dto;

import com.taskmanager.entity.TaskStatus;
import lombok.Data;

@Data
public class TaskRequestDTO {

    private String title;
    private String description;
    private TaskStatus status;
}