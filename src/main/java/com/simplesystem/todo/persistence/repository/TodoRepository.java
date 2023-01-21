package com.simplesystem.todo.persistence.repository;

import com.simplesystem.todo.persistence.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface TodoRepository extends JpaRepository<TodoEntity, Long> {

    List<TodoEntity> findAllByDoneAtIsNullAndDueAtIsAfter(OffsetDateTime now);
    default List<TodoEntity> findAllNotDone() {
        return findAllByDoneAtIsNullAndDueAtIsAfter(OffsetDateTime.now());
    }
}
