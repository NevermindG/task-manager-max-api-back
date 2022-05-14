package com.max.taskmanagermax_api.service;

import java.util.List;


import com.max.taskmanagermax_api.DTO.ProjectDTO;
import com.max.taskmanagermax_api.DTO.TaskDTO;
import com.max.taskmanagermax_api.entity.Task;

public interface TaskService {
    TaskDTO saveTask(long projectId, TaskDTO taskDTO);
    TaskDTO updateTask(long projectId, long taskId, TaskDTO taskRequest);
    
    TaskDTO findTaskById(long projectId, long taskId);
    List<TaskDTO> findTasksByProjectId(long projectId);
    
    void deleteTask(long projectId, long taskId);
}
