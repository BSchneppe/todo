package com.simplesystem.todo.models;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * A DTO for the {@link com.simplesystem.todo.persistence.entity.TodoEntity} entity
 */
public record TodoDto(Long id, OffsetDateTime createdAt, OffsetDateTime dueAt, OffsetDateTime doneAt,
                      String description,StatusEnum status) implements Serializable {
}
