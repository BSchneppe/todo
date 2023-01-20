package com.simplesystem.todo.models;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * A DTO for the {@link com.simplesystem.todo.persistence.entity.TodoEntity} entity
 */
public record TodoCreationDto(OffsetDateTime dueAt,String description) implements Serializable {
}
