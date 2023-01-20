package com.simplesystem.todo.persistence.repository;

import com.simplesystem.todo.persistence.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoEntityRepository extends JpaRepository<TodoEntity, Long> {
}
