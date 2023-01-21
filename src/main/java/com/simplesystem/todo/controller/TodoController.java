package com.simplesystem.todo.controller;

import com.simplesystem.todo.models.TodoCreationDto;
import com.simplesystem.todo.models.TodoDto;
import com.simplesystem.todo.models.TodoModificationDto;
import com.simplesystem.todo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.Problem;

@RestController()
@RequiredArgsConstructor
@ApiResponses({@ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class))}), @ApiResponse(responseCode = "404", description = "Not Found", content = {@Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class))}), @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class))})})
public class TodoController {
    private final TodoService todoService;

    @PostMapping(value = "todos", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "createTodo", summary = "Create a todo", responses = {@ApiResponse(responseCode = "200", description = "Created todo", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TodoDto.class))})})
    public ResponseEntity<TodoDto> createTodo(@Validated @RequestBody TodoCreationDto todoDto) {
        return ResponseEntity.ok(todoService.create(todoDto));
    }

    @PutMapping(value = "todos/{todoId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "updateTodo", summary = "Update a todo", responses = {@ApiResponse(responseCode = "200", description = "Updated todo", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TodoDto.class))})})
    public ResponseEntity<TodoDto> updateTodo(@PathVariable final Long todoId, @Validated @RequestBody TodoModificationDto todoModificationDto) {
        return ResponseEntity.ok(todoService.update(todoId, todoModificationDto));
    }


    @PatchMapping("todos/{todoId}/done")
    @Operation(operationId = "markTodoAsDone", summary = "Mark a todo as done", responses = {@ApiResponse(responseCode = "200", description = "Marked todo as done", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TodoDto.class))})})
    public ResponseEntity<TodoDto> markTodoAsDone(@PathVariable final Long todoId) {
        return ResponseEntity.ok(todoService.markAsDone(todoId));
    }

    @PatchMapping("todos/{todoId}/not-done")
    @Operation(operationId = "markTodoAsUndone", summary = "Mark a todo as undone", responses = {@ApiResponse(responseCode = "200", description = "Marked todo as undone", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TodoDto.class))})})
    public ResponseEntity<TodoDto> markTodoAsUndone(@PathVariable final Long todoId) {
        return ResponseEntity.ok(todoService.markAsNotDone(todoId));
    }

    @GetMapping(value = "todos/{todoId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "getTodo", summary = "Get a todo", responses = {@ApiResponse(responseCode = "200", description = "Get a todo", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TodoDto.class))})})
    public ResponseEntity<TodoDto> getTodoById(@PathVariable final Long todoId) {
        return ResponseEntity.ok(todoService.getById(todoId));
    }

    @GetMapping("todos")
    @Operation(operationId = "getAllTodos", summary = "Get all todos", responses = {@ApiResponse(responseCode = "200", description = "Get all todos", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TodoDto.class))})})
    public ResponseEntity<Iterable<TodoDto>> getAllTodos(@RequestParam(defaultValue = "true") final boolean onlyNotDone) {
        return ResponseEntity.ok(todoService.getAll(onlyNotDone));
    }

}
