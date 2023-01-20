package com.simplesystem.todo.persistence.repository;

import com.simplesystem.todo.persistence.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
}
