package com.max.taskmanagermax_api.service;


import java.util.List;

import com.max.taskmanagermax_api.DTO.ProjectDTO;
import com.max.taskmanagermax_api.DTO.ProjectResponseDTO;

public interface ProjectService {
    
    ProjectDTO saveProject(ProjectDTO projectDTO);
    
    ProjectDTO updateProject(ProjectDTO projectDTO, long id);
    
    ProjectResponseDTO findAllProjects(int numberPage, int sizePage, String sortBy, String sortDir);
    
    ProjectDTO findProjectById(long id);
    
    void deleteProject(long id);
    
    List<ProjectDTO> findAllUnpaginableProjects();
}
