package com.max.taskmanagermax_api.controller;

import java.util.*;


import com.max.taskmanagermax_api.DTO.CommentDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.max.taskmanagermax_api.service.CommentService;


import javax.validation.Valid;
import static com.max.taskmanagermax_api.utility.CrossOrigin.URL_CROSS_ORIGIN;

@RestController
@RequestMapping ("/api/")
@CrossOrigin(origins = URL_CROSS_ORIGIN)
public class CommentController {
    private final CommentService commentService;
    
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    
    
    @GetMapping("/tasks/{taskId}/comments/")
    public List<CommentDTO> listCommentsByTaskId(@PathVariable (value = "taskId") long taskId) {
        return commentService.findCommentsByTaskId(taskId);
    }
    
    @PostMapping ("/tasks/{taskId}/comments")
    public ResponseEntity<CommentDTO> saveComment(@PathVariable (value = "taskId") long taskId,
                                                    @Valid @RequestBody CommentDTO commentDTO) {
        return new ResponseEntity<>(commentService.saveComment(taskId, commentDTO), HttpStatus.CREATED);
    }
    
    @GetMapping ("/tasks/{taskId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable (value = "taskId") long taskId,
                                                        @PathVariable (value = "commentId") long commentId) {
        CommentDTO commentDTO = commentService.findCommentById(taskId, commentId);
        return new ResponseEntity<>(commentDTO, HttpStatus.OK);
    }
    
    
    @PutMapping ("/tasks/{taskId}/comments/{commentId}")
    //@ResponseBody
    public ResponseEntity<CommentDTO> updateComment(@PathVariable (value = "taskId") long taskId,
                                                    @PathVariable (value = "commentId") long commentId,
                                                    @Valid @RequestBody CommentDTO commentDTO) {
        
        CommentDTO updatedComment = commentService.updateComment(taskId, commentId, commentDTO);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }
    
    @DeleteMapping("/tasks/{taskId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable (name = "taskId") long taskId,
                                                @PathVariable (name = "commentId") long commentId) {
        commentService.deleteComment(taskId, commentId);
        return new ResponseEntity<>("Comentario eliminado con éxito", HttpStatus.OK);
    }
    
}
