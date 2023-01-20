package com.simplesystem.todo.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.time.OffsetDateTime;

@Entity(name = "todo")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoEntity extends AbstractPersistable<Long> {

    @CreationTimestamp
    @Column(updatable = false, nullable = false, name = "created_at")
    private OffsetDateTime createdAt;

    @Column(nullable = false, name = "due_at")
    private OffsetDateTime dueAt;

    @Column(name = "done_at")
    private OffsetDateTime doneAt;

    @Column(nullable = false, name = "description")
    private String description;

}
