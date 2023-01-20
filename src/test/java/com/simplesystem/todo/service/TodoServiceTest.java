package com.simplesystem.todo.service;

import com.simplesystem.todo.mapper.TodoMapper;
import com.simplesystem.todo.models.TodoCreationDto;
import com.simplesystem.todo.models.TodoModificationDto;
import com.simplesystem.todo.persistence.entity.TodoEntity;
import com.simplesystem.todo.persistence.repository.TodoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zalando.problem.Status;
import org.zalando.problem.ThrowableProblem;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    private static final TodoEntity VALID_TODO_ENTITY = TodoEntity.builder().description("test").dueAt(OffsetDateTime.now().plus(1, ChronoUnit.DAYS)).build();
    private static final TodoEntity PAST_DUE_VALID_TODO_ENTITY = TodoEntity.builder().description("test").dueAt(OffsetDateTime.now()).build();
    private static final long VALID_ID = 1L;
    private static final long PAST_DUE_ID = 2L;
    @Mock
    private TodoRepository todoRepository;

    @Spy
    private TodoMapper todoMapper = Mappers.getMapper(TodoMapper.class);
    @InjectMocks
    private TodoService tested;

    @Test
    void create() {
        TodoCreationDto todoCreationDto = new TodoCreationDto(OffsetDateTime.now(), "test");
        tested.create(todoCreationDto);
        verify(todoMapper, times(1)).toEntity(any(TodoCreationDto.class));
        verify(todoRepository, times(1)).save(any(TodoEntity.class));
    }

    @Test
    void update() {
        //given
        when(todoRepository.findById(VALID_ID)).thenReturn(Optional.of(VALID_TODO_ENTITY));
        when(todoRepository.findById(PAST_DUE_ID)).thenReturn(Optional.of(PAST_DUE_VALID_TODO_ENTITY));

        //when
        TodoModificationDto todoModificationDto = new TodoModificationDto("test");
        tested.update(VALID_ID, todoModificationDto);
        ThrowableProblem pastDue = assertThrows(ThrowableProblem.class, () -> tested.update(PAST_DUE_ID, todoModificationDto));
        ThrowableProblem notFound = assertThrows(ThrowableProblem.class, () -> tested.update(3L, todoModificationDto));

        //then
        verify(todoMapper, times(1)).partialUpdate(any(TodoModificationDto.class), any(TodoEntity.class));
        verify(todoRepository, times(1)).save(any(TodoEntity.class));
        Assertions.assertEquals(Status.CONFLICT, pastDue.getStatus());
        Assertions.assertEquals(Status.NOT_FOUND, notFound.getStatus());
    }

    @Test
    void getById() {
        //given
        when(todoRepository.findById(VALID_ID)).thenReturn(Optional.of(VALID_TODO_ENTITY));
        when(todoRepository.findById(PAST_DUE_ID)).thenReturn(Optional.of(PAST_DUE_VALID_TODO_ENTITY));
        //when
        tested.getById(VALID_ID);
        tested.getById(PAST_DUE_ID);
        ThrowableProblem notFound = assertThrows(ThrowableProblem.class, () -> tested.getById(3L));
        //then
        verify(todoRepository, times(1)).findById(VALID_ID);
        verify(todoRepository, times(1)).findById(PAST_DUE_ID);
        Assertions.assertEquals(Status.NOT_FOUND, notFound.getStatus());
    }

    @Test
    void markAsDone() {
        //given
        when(todoRepository.findById(VALID_ID)).thenReturn(Optional.of(VALID_TODO_ENTITY));
        when(todoRepository.findById(PAST_DUE_ID)).thenReturn(Optional.of(PAST_DUE_VALID_TODO_ENTITY));
        //when
        tested.markAsDone(VALID_ID);
        ThrowableProblem pastDue = assertThrows(ThrowableProblem.class, () -> tested.markAsDone(PAST_DUE_ID));
        ThrowableProblem notFound = assertThrows(ThrowableProblem.class, () -> tested.markAsDone(3L));
        //then
        verify(todoRepository, times(1)).save(any(TodoEntity.class));
        Assertions.assertEquals(Status.CONFLICT, pastDue.getStatus());
        Assertions.assertEquals(Status.NOT_FOUND, notFound.getStatus());

    }

    @Test
    void markAsNotDone() {
        //given
        when(todoRepository.findById(VALID_ID)).thenReturn(Optional.of(VALID_TODO_ENTITY));
        when(todoRepository.findById(PAST_DUE_ID)).thenReturn(Optional.of(PAST_DUE_VALID_TODO_ENTITY));
        //when
        tested.markAsNotDone(VALID_ID);
        ThrowableProblem pastDue = assertThrows(ThrowableProblem.class, () -> tested.markAsNotDone(PAST_DUE_ID));
        ThrowableProblem notFound = assertThrows(ThrowableProblem.class, () -> tested.markAsNotDone(3L));
        //then
        verify(todoRepository, times(1)).save(any(TodoEntity.class));
        Assertions.assertEquals(Status.CONFLICT, pastDue.getStatus());
        Assertions.assertEquals(Status.NOT_FOUND, notFound.getStatus());
    }

    @Test
    void getAll() {
        tested.getAll(false);
        verify(todoRepository, times(1)).findAll();
    }

    @Test
    void getAllNotDone() {
        tested.getAll(true);
        verify(todoRepository, times(1)).findAllNotDone();
    }
}
