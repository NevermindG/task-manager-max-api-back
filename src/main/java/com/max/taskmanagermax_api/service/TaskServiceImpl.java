package com.max.taskmanagermax_api.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


import com.max.taskmanagermax_api.DTO.TaskDTO;
import com.max.taskmanagermax_api.entity.Project;
import com.max.taskmanagermax_api.entity.User;
import com.max.taskmanagermax_api.exceptions.MaxAppException;
import com.max.taskmanagermax_api.exceptions.ResourceNotFoundException;
import com.max.taskmanagermax_api.repository.ProjectRepository;
import com.max.taskmanagermax_api.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.max.taskmanagermax_api.entity.Task;
import com.max.taskmanagermax_api.repository.TaskRepository;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final ProjectRepository projectRepository;


    private final ModelMapper    modelMapper;
    private final UserRepository userRepository;

    public TaskServiceImpl(TaskRepository taskRepository, ProjectRepository projectRepository, ModelMapper modelMapper, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Override
    public TaskDTO saveTask(long projectId, TaskDTO taskDTO) {
        
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 0);
        date = cal.getTime();
        
        Task task = mappingEntity(taskDTO);
        Project project = projectRepository
                .findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));
        task.setProyecto(project);
        task.setFechaRegistro(date);
        task.setEstado(1);
    
        var startDate = task.getFechaRegistro().getTime();
        var endDate = task.getFechaFinaliza().getTime();
        var difference = endDate - startDate;
        var days = Math.floor(difference / (1000 * 60 * 60 * 24));
        
        if (days <= 2) {
            task.setEstado(2);
        }
        
        Set<User> user = new HashSet<>();
        
        for (int i = 0; i < taskDTO.getNameUser().size(); i++) {
            user.add(userRepository.findByUsername((taskDTO.getNameUser().get(i))));
        }
        
        task.setUsuarios(user);
        
        if (task.getFechaFinaliza().before(date)) {
            throw new MaxAppException(HttpStatus.BAD_REQUEST, "La tarea tiene que programarse un día después de la fecha esperada");
        } else {
            cal.setTime(task.getFechaFinaliza());
            cal.add(Calendar.DATE, 1);
            task.setFechaFinaliza(cal.getTime());
        }
        Task newTask = taskRepository.save(task);
        return mappingDTO(newTask);
    }

    @Override
    public TaskDTO updateTask(long projectId, long taskId, TaskDTO taskRequest) {
    
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 0);
        date = cal.getTime();

        ProjectTask(projectId, taskId).setNombreTarea(taskRequest.getNombreTarea());
        ProjectTask(projectId, taskId).setContenidoTarea(taskRequest.getContenidoTarea());
        ProjectTask(projectId, taskId).setFechaFinaliza(taskRequest.getFechaFinaliza());
        ProjectTask(projectId, taskId).setEstado(1);
        //ProjectTask(projectId, taskId).setFechaRegistro(date);
    
        var startDate = ProjectTask(projectId, taskId).getFechaRegistro().getTime();
        var endDate = ProjectTask(projectId, taskId).getFechaFinaliza().getTime();
        var difference = endDate - startDate;
        var days = Math.floor(difference / (1000 * 60 * 60 * 24));
    
        if (days <= 2) {
            ProjectTask(projectId, taskId).setEstado(2);
        }
    
        Set<User> user = new HashSet<>();
    
        for (int i = 0; i < taskRequest.getNameUser().size(); i++) {
            user.add(userRepository.findByUsername((taskRequest.getNameUser().get(i))));
        }
    
        ProjectTask(projectId, taskId).setUsuarios(user);

        if (ProjectTask(projectId, taskId).getFechaFinaliza().before(date)) {
            throw new MaxAppException(HttpStatus.BAD_REQUEST, "La tarea tiene que programarse un día después de la fecha esperada");
        } else {
            cal.setTime(ProjectTask(projectId, taskId).getFechaFinaliza());
            cal.add(Calendar.DATE, 1);
            ProjectTask(projectId, taskId).setFechaFinaliza(cal.getTime());
        }
        
        Task updatedTask = taskRepository.save(ProjectTask(projectId, taskId));
        return mappingDTO(updatedTask);
    }

    @Override
    public TaskDTO findTaskById(long projectId, long taskId) {

        return mappingDTO(ProjectTask(projectId, taskId));

    }

    @Override
    public List<TaskDTO> findTasksByProjectId(long projectId) {
        List<Task> tasks = taskRepository.findByProyectoIdProyecto(projectId);
        return tasks.stream()
                .map(this::mappingDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTask(long projectId, long taskId) {
        Project project = projectRepository
                .findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));

        Task task = taskRepository
                .findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));
        if (!task.getProyecto().getIdProyecto().equals(project.getIdProyecto())) {
            throw new MaxAppException(HttpStatus.BAD_REQUEST, "El comentario no es del proyecto");
        }

        taskRepository.delete(task);

    }

    private TaskDTO mappingDTO(Task task) {
        return modelMapper.map(task, TaskDTO.class);
    }

    private Task mappingEntity(TaskDTO taskDTO) {
        return modelMapper.map(taskDTO, Task.class);
    }

    private Task ProjectTask(long projectId, long taskId) {
        Project project = projectRepository
                .findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));

        Task task = taskRepository
                .findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

        if (!task.getProyecto().getIdProyecto().equals(project.getIdProyecto())) {
            throw new MaxAppException(HttpStatus.BAD_REQUEST, "La tarea no pertenece al proyecto");
        }

        return task;
    }

}
