package com.simplesystem.todo.models;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * A DTO for the {@link com.simplesystem.todo.persistence.entity.TodoEntity} entity
 */
public record TodoCreationDto(@Future @NotNull OffsetDateTime dueAt, @NotBlank String description) implements Serializable {
}
