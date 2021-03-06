package com.max.taskmanagermax_api.controller;

import java.util.HashMap;
import java.util.List;


import com.max.taskmanagermax_api.DTO.TaskDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.max.taskmanagermax_api.service.TaskService;


import javax.validation.Valid;
import static com.max.taskmanagermax_api.utility.CrossOrigin.URL_CROSS_ORIGIN;

@RestController
@RequestMapping ("/api/")
@CrossOrigin(origins = URL_CROSS_ORIGIN)
public class TaskController {
    private final TaskService taskService;
    
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    
    
    @GetMapping ("projects/{projectId}/tasks/{taskId}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable (value = "projectId") long projectId,
                                            @PathVariable (value = "taskId") long taskId) {
        TaskDTO task = taskService.findTaskById(projectId, taskId);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }
    
    @GetMapping ("projects/{projectId}/tasks")
    public List<TaskDTO> getTasks(@PathVariable (value = "projectId") long projectId) {
        return taskService.findTasksByProjectId(projectId);
    }
    
    @PostMapping ("projects/{projectId}/tasks")
    public ResponseEntity<TaskDTO> createTask(@PathVariable (value = "projectId") long projectId,
                                                @Valid @RequestBody TaskDTO taskDTO) {
        
        return new ResponseEntity<>(taskService.saveTask(projectId, taskDTO), HttpStatus.CREATED);
    }
    
    @PutMapping ("projects/{projectId}/tasks/{taskId}")
    @ResponseBody
    public ResponseEntity<TaskDTO> updateTask(@PathVariable (value = "projectId") long projectId,
                                                @PathVariable (value = "taskId") long taskId,
                                                @RequestBody TaskDTO taskDTO) {
        TaskDTO updateTasktById = taskService.updateTask(projectId, taskId, taskDTO);
        return new ResponseEntity<>(updateTasktById, HttpStatus.OK);
    }
    
    @PutMapping ("projects/{projectId}/tasks/{taskId}/status/{std}")
    @ResponseBody
    public ResponseEntity<TaskDTO> updateStatusTask(@PathVariable (value = "projectId") long projectId,
                                                @PathVariable (value = "taskId") long taskId,
                                                @PathVariable (value = "std") long std//,
                                                /*@RequestBody TaskDTO taskDTO*/) {
        TaskDTO updateTasktById = taskService.updateStatus(projectId, taskId, std);
        return new ResponseEntity<>(updateTasktById, HttpStatus.OK);
    }
    
    @DeleteMapping ("projects/{projectId}/tasks/{taskId}")
    public ResponseEntity<HashMap<String, Object>> deleteTask(@PathVariable (value = "projectId") long projectId,
                                                @PathVariable (value = "taskId") long taskId) {
        taskService.deleteTask(projectId, taskId);
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "Tarea eliminada con ??xito");
        response.put("status", HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
