package com.simplesystem.todo.models;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * A DTO for the {@link com.simplesystem.todo.persistence.entity.TodoEntity} entity
 */
public record TodoModificationDto(@NotBlank String description) implements Serializable {
}
