package com.taskmanager.controller;

import com.taskmanager.entity.Task;
import com.taskmanager.entity.TaskStatus;
import com.taskmanager.entity.User;
import com.taskmanager.security.AuthUtil;
import com.taskmanager.service.TaskService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.taskmanager.entity.TaskHistory;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;
    private final AuthUtil authUtil;

    public TaskController(
            TaskService service,
            AuthUtil authUtil
    ) {
        this.service = service;
        this.authUtil = authUtil;
    }

    // ---------------- SUPERVISOR ----------------

    @PostMapping("/create")
    @PreAuthorize("hasRole('SUPERVISOR')")
    public Task createTask(
            @RequestBody Task task,
            @RequestParam Long supervisorId,
            @RequestParam Long employeeId
    ) {
        return service.createTask(task, supervisorId, employeeId);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('SUPERVISOR')")
    public List<Task> getAll() {
        return service.getAllTasks();
    }

    @PutMapping("/{id}/done")
    @PreAuthorize("hasRole('SUPERVISOR')")
    public Task confirmDone(@PathVariable Long id) {
        return service.confirmDone(id);
    }

    // ---------------- EMPLOYEE ----------------

    @GetMapping("/employee/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public List<Task> employeeTasks(@PathVariable Long id) {
        return service.getTasksForEmployee(id);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public Task updateStatus(
            @PathVariable Long id,
            @RequestParam TaskStatus status,
            Authentication authentication
    ) {
        User user = authUtil.getLoggedInUser(authentication);

        return service.updateStatus(id, status, user);
    }

    @PutMapping("/{id}/complete")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public Task completeTask(
            @PathVariable Long id,
            Authentication authentication
    ) {
        User user = authUtil.getLoggedInUser(authentication);

        return service.updateStatus(
                id,
                TaskStatus.COMPLETED,
                user
        );
    }

    @GetMapping("/{id}/history")
    @PreAuthorize("hasAnyRole('SUPERVISOR','EMPLOYEE')")
    public List<TaskHistory> getHistory(@PathVariable Long id) {
    return service.getTaskHistory(id);
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('SUPERVISOR','EMPLOYEE')")
    public List<Task> filterTasks(@RequestParam TaskStatus status) {
    return service.filterByStatus(status);
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('SUPERVISOR')")
    public Map<String, Long> getStats() {
    return service.getStats();
    }

}