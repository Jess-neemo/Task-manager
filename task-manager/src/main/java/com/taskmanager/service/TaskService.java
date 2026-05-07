package com.taskmanager.service;

import com.taskmanager.entity.*;
import com.taskmanager.repository.TaskHistoryRepository;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {

    private final TaskRepository taskRepo;
    private final UserRepository userRepo;
    private final TaskHistoryRepository historyRepo;

    public TaskService(
            TaskRepository taskRepo,
            UserRepository userRepo,
            TaskHistoryRepository historyRepo
    ) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
        this.historyRepo = historyRepo;
    }

    // ================= CREATE TASK =================
    public Task createTask(Task task, Long supervisorId, Long employeeId) {

        User supervisor = userRepo.findById(supervisorId).orElseThrow();
        User employee = userRepo.findById(employeeId).orElseThrow();

        task.setCreatedBy(supervisor);
        task.setAssignedTo(employee);
        task.setStatus(TaskStatus.CREATED);
        task.setCreatedAt(LocalDateTime.now());

        return taskRepo.save(task);
    }

    // ================= GET TASKS =================
    public List<Task> getTasksForEmployee(Long userId) {

        User user = userRepo.findById(userId).orElseThrow();
        return taskRepo.findByAssignedTo(user);
    }

    public List<Task> getAllTasks() {
        return taskRepo.findAll();
    }

    public List<Task> filterByStatus(TaskStatus status) {
        return taskRepo.findByStatus(status);
    }

    // ================= UPDATE STATUS =================
    public Task updateStatus(Long taskId, TaskStatus newStatus, User user) {

        Task task = taskRepo.findById(taskId).orElseThrow();

        TaskStatus oldStatus = task.getStatus();

        if (!isValidTransition(oldStatus, newStatus)) {
            throw new RuntimeException("Invalid task status transition");
        }

        task.setStatus(newStatus);
        taskRepo.save(task);

        TaskHistory history = new TaskHistory();
        history.setTask(task);
        history.setChangedBy(user);
        history.setOldStatus(oldStatus.name());
        history.setNewStatus(newStatus.name());
        history.setAction("STATUS_UPDATED");
        history.setChangedAt(LocalDateTime.now());

        historyRepo.save(history);

        return task;
    }

    // ================= TASK HISTORY =================
    public List<TaskHistory> getTaskHistory(Long taskId) {
        Task task = taskRepo.findById(taskId).orElseThrow();
        return task.getHistory();
    }

    // ================= CONFIRM DONE =================
    public Task confirmDone(Long taskId) {

        Task task = taskRepo.findById(taskId).orElseThrow();
        task.setStatus(TaskStatus.DONE);

        return taskRepo.save(task);
    }

    // ================= VALIDATION =================
    private boolean isValidTransition(TaskStatus current, TaskStatus next) {

        return switch (current) {

            case CREATED -> next == TaskStatus.ASSIGNED;
            case ASSIGNED -> next == TaskStatus.IN_PROGRESS;
            case IN_PROGRESS -> next == TaskStatus.COMPLETED;
            case COMPLETED -> next == TaskStatus.DONE;
            default -> false;
        };
    }

    // ================= STATS =================
    public Map<String, Long> getStats() {

        Map<String, Long> stats = new HashMap<>();

        stats.put("TOTAL", taskRepo.count());
        stats.put("CREATED", taskRepo.countByStatus(TaskStatus.CREATED));
        stats.put("IN_PROGRESS", taskRepo.countByStatus(TaskStatus.IN_PROGRESS));
        stats.put("COMPLETED", taskRepo.countByStatus(TaskStatus.COMPLETED));
        stats.put("DONE", taskRepo.countByStatus(TaskStatus.DONE));

        return stats;
    }
}