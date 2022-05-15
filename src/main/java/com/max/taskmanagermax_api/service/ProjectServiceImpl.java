package com.max.taskmanagermax_api.service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


import com.max.taskmanagermax_api.DTO.ProjectDTO;
import com.max.taskmanagermax_api.DTO.ProjectResponseDTO;
import com.max.taskmanagermax_api.entity.User;
import com.max.taskmanagermax_api.exceptions.MaxAppException;
import com.max.taskmanagermax_api.exceptions.ResourceNotFoundException;
import com.max.taskmanagermax_api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import com.max.taskmanagermax_api.entity.Project;
import com.max.taskmanagermax_api.repository.ProjectRepository;

@Slf4j
@Service
public class ProjectServiceImpl implements ProjectService {
    
    private final ModelMapper       modelMapper;
    private final ProjectRepository projectRepository;
    private final UserRepository    userRepository;
    
    @Autowired
    public ProjectServiceImpl(ModelMapper modelMapper, ProjectRepository projectRepository, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }
    
    @Override
    public ProjectDTO saveProject(ProjectDTO projectDTO) {
        
        
        Project project = mappingEntity(projectDTO);
        project.setFechaRegistro(new Date());
        project.setEstado(1);
        Set<User> user = new HashSet<>();
        
        for (int i = 0; i < projectDTO.getNameUser().size(); i++) {
            user.add(userRepository.findByUsername((projectDTO.getNameUser().get(i))));
        }
        
        project.setUsuarios(user);
        
        
        if (projectRepository.existsByNombreProyecto(project.getNombreProyecto())) {
            throw new MaxAppException(HttpStatus.BAD_REQUEST, "El nombre ya existe");
        } else if (project.getFechaFinaliza().before(new Date())) {
            throw new MaxAppException(HttpStatus.BAD_REQUEST, "El proyecto tiene que programarse un día después de la fecha esperada");
        } else {
            project.setNombreProyecto(project.getNombreProyecto());
            project.setFechaFinaliza(project.getFechaFinaliza());
        }
        
        Project newProject = projectRepository.save(project);
        
        
        return mappingDTO(newProject);
    }
    
    @Override
    public ProjectDTO updateProject(ProjectDTO projectDTO, long id) {
        
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publication", "id", id));
        
        project.setEstado(projectDTO.getEstado());
        project.setFechaRegistro(new Date());
        project.setEstado(1);
        project.setNombreProyecto(projectDTO.getNombreProyecto());
        project.setFechaFinaliza(projectDTO.getFechaFinaliza());
        
        Set<User> user = new HashSet<>();
        
        for (int i = 0; i < projectDTO.getNameUser().size(); i++) {
            user.add(userRepository.findByUsername((projectDTO.getNameUser().get(i))));
        }
        
        if (project.getFechaFinaliza().before(new Date())) {
            throw new MaxAppException(HttpStatus.BAD_REQUEST, "El proyecto tiene que programarse un día después de la fecha esperada");
        } else {
            project.setFechaFinaliza(project.getFechaFinaliza());
        }
        
        project.setUsuarios(user);
        
        Project updateProject = projectRepository.save(project);
        return mappingDTO(updateProject);
    }
    
    @Override
    public ProjectResponseDTO findAllProjects(int numberPage, int sizePage, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(numberPage, sizePage, sort);
        Page<Project> projects = projectRepository.findAll(pageable);
        
        List<Project> listOfProjects = projects.getContent();
        List<ProjectDTO> content = listOfProjects.stream()
                .map(this::mappingDTO)
                .collect(Collectors.toList());
        
        ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO();
        projectResponseDTO.setContent(content);
        projectResponseDTO.setNumberPage(projects.getNumber());
        projectResponseDTO.setSizePage(projects.getSize());
        projectResponseDTO.setTotalElements(projects.getTotalElements());
        projectResponseDTO.setTotalPages(projects.getTotalPages());
        projectResponseDTO.setLast(projects.isLast());
        
        return projectResponseDTO;
        
    }
    
    @Override
    public ProjectDTO findProjectById(long id) {
        Project project = projectRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Project", "id", id));
        return mappingDTO(project);
    }
    
    @Override
    public void deleteProject(long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
        projectRepository.delete(project);
        
    }
    
    private ProjectDTO mappingDTO(Project proyecto) {
        return modelMapper.map(proyecto, ProjectDTO.class);
    }
    
    private Project mappingEntity(ProjectDTO projectDTO) {
        return modelMapper.map(projectDTO, Project.class);
    }
    
}
