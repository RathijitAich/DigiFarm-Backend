package com.example.demo.controller;

import com.example.demo.entity.Field;
import com.example.demo.entity.Task;
import com.example.demo.repositories.FieldRepo;
import com.example.demo.repositories.TaskRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:3000")
public class TaskController {

    private final TaskRepo taskRepo;
    private final FieldRepo fieldRepo;

    public TaskController(TaskRepo taskRepo, FieldRepo fieldRepo) {
        this.taskRepo = taskRepo;
        this.fieldRepo = fieldRepo;
    }

    @PostMapping("/{fieldId}")
    public Task addTask(@PathVariable Long fieldId, @RequestBody Task task) {
        Field field = fieldRepo.findById(fieldId).orElseThrow();
        task.setField(field);
        task.setStatus("PENDING");
        return taskRepo.save(task);
    }

    @GetMapping("/field/{fieldId}")
    public List<Task> getTasks(@PathVariable Long fieldId) {
        return taskRepo.findByField_FieldId(fieldId);
    }
}
