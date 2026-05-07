package com.taskmanager.dto;

import com.taskmanager.entity.TaskStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskResponseDTO {

    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private String assignedTo;
    private String createdBy;
}