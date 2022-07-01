package com.max.taskmanagermax_api.service;

import java.util.List;


import com.max.taskmanagermax_api.DTO.TaskDTO;

public interface TaskService {
    TaskDTO saveTask(long projectId, TaskDTO taskDTO);
    TaskDTO updateTask(long projectId, long taskId, TaskDTO taskRequest);

    TaskDTO findTaskById(long projectId, long taskId);
    List<TaskDTO> findTasksByProjectId(long projectId);

    void deleteTask(long projectId, long taskId);
    
    TaskDTO updateStatus(long projectId, long taskId, long tsd);
}
