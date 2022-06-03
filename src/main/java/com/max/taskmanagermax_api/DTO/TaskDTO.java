package com.max.taskmanagermax_api.DTO;

import java.util.Date;
import java.util.List;
import java.util.Set;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.max.taskmanagermax_api.entity.User;
import lombok.*;

@Getter
@Setter
public class TaskDTO {
    
    private long   idTarea;
    private String nombreTarea;
    private String contenidoTarea;
    private Date   fechaRegistro;
    private Date   fechaFinaliza;
    private int    estado;
    
    @JsonProperty (access = JsonProperty.Access.WRITE_ONLY)
    private List<String> nameUser;
    private     Set<User>    usuarios;
}
