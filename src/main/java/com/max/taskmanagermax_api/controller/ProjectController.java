package com.max.taskmanagermax_api.controller;


import com.max.taskmanagermax_api.DTO.ProjectDTO;
import com.max.taskmanagermax_api.DTO.ProjectResponseDTO;
import com.max.taskmanagermax_api.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.max.taskmanagermax_api.service.ProjectService;


import javax.validation.Valid;
import static com.max.taskmanagermax_api.utility.CrossOrigin.URL_CROSS_ORIGIN;
import static com.max.taskmanagermax_api.utility.PaginationConstants.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping ("/api/projects")
@CrossOrigin(origins = URL_CROSS_ORIGIN)
@SuppressWarnings ({"all"})
public class ProjectController {
    private final ProjectService projectService;
    
    private final UserRepository userRepository;
    
    public ProjectController(ProjectService projectService, UserRepository userRepository) {
        this.projectService = projectService;
        this.userRepository = userRepository;
    }

    @GetMapping("/listAll")
    public List<ProjectDTO> findAllUnpaginableProjects() {
        return projectService.findAllUnpaginableProjects();
    }
    
    @GetMapping
    public ProjectResponseDTO listProjects(
                                @RequestParam (value = "pageNo", defaultValue = NUMBER_OF_PAGE_BY_DEFAULT, required = false) int pageNumber,
                                @RequestParam (value = "pageSize", defaultValue = SIZE_OF_PAGE_BY_DEFAULT, required = false) int pageSize,
                                @RequestParam (value = "sortBy", defaultValue = ORDER_BY_DEFAULT, required = false) String sortById,
                                @RequestParam (value = "sortDir", defaultValue = ORDER_DIRECTION_BY_DEFAULT, required = false) String sortDir) {
        return projectService.findAllProjects(pageNumber, pageSize, sortById, sortDir);
    }
    
    @PreAuthorize ("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProjectDTO> saveProject(@Valid @RequestBody ProjectDTO projectDTO) {
        return new ResponseEntity<>(projectService.saveProject(projectDTO), HttpStatus.OK);
    }
    
    @PreAuthorize ("hasRole('ADMIN')")
    @PutMapping ("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@Valid @RequestBody ProjectDTO projectDTO,
                                                    @PathVariable (name = "id") long id) {
        ProjectDTO projectResponse = projectService.updateProject(projectDTO, id);
        return new ResponseEntity<>(projectResponse, HttpStatus.OK);
    }
    
    @GetMapping ("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable (name = "id") long id) {
        return ResponseEntity.ok(projectService.findProjectById(id));
    }
    
    @PreAuthorize ("hasRole('ADMIN')")
    @DeleteMapping ("/{id}")
    public ResponseEntity<HashMap<String, Object>> deleteProject(@PathVariable (name = "id") long id) {
        projectService.deleteProject(id);
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "Proyecto eliminado con éxito");
        response.put("status", HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
