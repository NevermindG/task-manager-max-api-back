package com.max.taskmanagermax_api.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


import com.max.taskmanagermax_api.DTO.CommentDTO;
import com.max.taskmanagermax_api.entity.Task;
import com.max.taskmanagermax_api.exceptions.MaxAppException;
import com.max.taskmanagermax_api.exceptions.ResourceNotFoundException;
import com.max.taskmanagermax_api.repository.TaskRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.max.taskmanagermax_api.entity.Comment;
import com.max.taskmanagermax_api.repository.CommentRepository;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final ModelMapper modelMapper;

    private final TaskRepository taskRepository;

    public CommentServiceImpl(CommentRepository commentRepository, ModelMapper modelMapper, TaskRepository taskRepository) {
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
        this.taskRepository = taskRepository;
    }
    

    @Override
    public CommentDTO saveComment(long taskId, CommentDTO commentDTO) {

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 0);
        date = cal.getTime();
        
        Comment comment = mappingEntity(commentDTO);
        Task task = taskRepository
                .findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));
        comment.setTarea(task);
        comment.setContenidoComentario(commentDTO.getContenido());
        comment.setFechaRegistro(date);
        Comment newComment = commentRepository.save(comment);
        return mappingDTO(newComment);
    }

    @Override
    public CommentDTO updateComment(long taskId, long commentId, CommentDTO commentRequest) {
    
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 0);
        date = cal.getTime();
        
        TaskCommentUser(taskId, commentId).setContenidoComentario(commentRequest.getContenido());
        TaskCommentUser(taskId, commentId).setFechaRegistro(date);

        Comment updatedComment = commentRepository.save(TaskCommentUser(taskId, commentId));
        return mappingDTO(updatedComment);
    }

    @Override
    public CommentDTO findCommentById(long taskId, long commentId) {
        return mappingDTO(TaskCommentUser(taskId, commentId));
    }

    @Override
    public List<CommentDTO> findCommentsByTaskId(long taskId) {
        List<Comment> comments = commentRepository.findByTareaIdTarea(taskId);
        return comments.stream()
                .map(this::mappingDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteComment(long taskId, long commentId) {
        TaskCommentUser(taskId, commentId);
        commentRepository.delete(TaskCommentUser(taskId, commentId));
    }


    private CommentDTO mappingDTO(Comment comment) {
        return modelMapper.map(comment, CommentDTO.class);
    }

    private Comment mappingEntity(CommentDTO commentDTO) {
        return modelMapper.map(commentDTO, Comment.class);
    }

    private Comment TaskCommentUser (long taskId, long commentId) {
        Task task = taskRepository
                .findById(taskId)
                .orElseThrow(()-> new ResourceNotFoundException("Task", "id", taskId));
        
        Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        if (!comment.getTarea().getIdTarea().equals(task.getIdTarea())) {
            throw new MaxAppException(HttpStatus.BAD_REQUEST, "El comentario no pertenece a la tarea");
        }
        
        return comment;
    }


}
