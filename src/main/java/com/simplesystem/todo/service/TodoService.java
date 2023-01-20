package com.simplesystem.todo.service;

import com.simplesystem.todo.mapper.TodoMapper;
import com.simplesystem.todo.models.TodoCreationDto;
import com.simplesystem.todo.models.TodoDto;
import com.simplesystem.todo.models.TodoModificationDto;
import com.simplesystem.todo.persistence.entity.TodoEntity;
import com.simplesystem.todo.persistence.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoMapper todoMapper;

    private final TodoRepository todoRepository;

    public TodoDto create(TodoCreationDto todoCreationDto) {
        TodoEntity todoEntity = todoRepository.save(todoMapper.toEntity(todoCreationDto));
        return todoMapper.toDto(todoEntity);
    }

    public TodoDto update(Long id, TodoModificationDto todoModificationDto) {
        final TodoEntity todoEntity = todoRepository.findById(id).orElseThrow(() -> Problem.builder().withStatus(Status.NOT_FOUND).build());
        ensureMutable(todoEntity);
        TodoEntity todo = todoRepository.save(todoMapper.partialUpdate(todoModificationDto, todoEntity));
        return todoMapper.toDto(todo);
    }

    public TodoDto getById(Long id) {
        TodoEntity todoEntity = todoRepository.findById(id).orElseThrow(() -> Problem.builder().withStatus(Status.NOT_FOUND).build());
        return todoMapper.toDto(todoEntity);
    }

    public TodoDto markAsDone(Long id) {
        TodoEntity todoEntity = todoRepository.findById(id).orElseThrow(() -> Problem.builder().withStatus(Status.NOT_FOUND).build());
        ensureMutable(todoEntity);
        todoEntity.setDoneAt(OffsetDateTime.now());
        todoRepository.save(todoEntity);
        return todoMapper.toDto(todoEntity);
    }

    public TodoDto markAsNotDone(Long id) {
        TodoEntity todoEntity = todoRepository.findById(id).orElseThrow(() -> Problem.builder().withStatus(Status.NOT_FOUND).build());
        ensureMutable(todoEntity);
        todoEntity.setDoneAt(null);
        todoRepository.save(todoEntity);
        return todoMapper.toDto(todoEntity);
    }

    public List<TodoDto> getAll(final boolean onlyNotDone) {
        final List<TodoEntity> todos = onlyNotDone ? todoRepository.findAllNotDone() : todoRepository.findAll();
        return todoMapper.toDtos(todos);
    }

    private void ensureMutable(TodoEntity todoEntity) {
        if (todoEntity.getDueAt() != null && todoEntity.getDueAt().isBefore(OffsetDateTime.now())) {
            throw Problem.builder().withStatus(Status.CONFLICT).build();
        }
    }

}
