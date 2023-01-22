package com.simplesystem.todo.service;

import com.simplesystem.todo.mapper.TodoMapper;
import com.simplesystem.todo.models.TodoCreationDto;
import com.simplesystem.todo.models.TodoDto;
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

        //when
        TodoModificationDto todoModificationDto = new TodoModificationDto("test");
        tested.update(VALID_ID, todoModificationDto);

        //then
        verify(todoMapper, times(1)).partialUpdate(any(TodoModificationDto.class), any(TodoEntity.class));
        verify(todoRepository, times(1)).save(any(TodoEntity.class));
    }

    @Test
    void updateNotFound() {
        //when
        TodoModificationDto todoModificationDto = new TodoModificationDto("test");
        ThrowableProblem notFound = assertThrows(ThrowableProblem.class, () -> tested.update(3L, todoModificationDto));
        //then
        Assertions.assertEquals(Status.NOT_FOUND, notFound.getStatus());
    }

    @Test
    void updatePastDue() {
        //given
        when(todoRepository.findById(PAST_DUE_ID)).thenReturn(Optional.of(PAST_DUE_VALID_TODO_ENTITY));
        //when
        TodoModificationDto todoModificationDto = new TodoModificationDto("test");
        ThrowableProblem pastDue = assertThrows(ThrowableProblem.class, () -> tested.update(PAST_DUE_ID, todoModificationDto));
        //then
        Assertions.assertEquals(Status.CONFLICT, pastDue.getStatus());
    }

    @Test
    void getById() {
        //given
        when(todoRepository.findById(VALID_ID)).thenReturn(Optional.of(VALID_TODO_ENTITY));
        //when
        tested.getById(VALID_ID);
        //then
        verify(todoRepository, times(1)).findById(VALID_ID);
    }

    @Test
    void getByIdPastDue() {
        //given
        when(todoRepository.findById(PAST_DUE_ID)).thenReturn(Optional.of(PAST_DUE_VALID_TODO_ENTITY));
        //when
        TodoDto todo = tested.getById(PAST_DUE_ID);
        //then
        Assertions.assertEquals(PAST_DUE_VALID_TODO_ENTITY.getDescription(), todo.description());
        Assertions.assertEquals(PAST_DUE_VALID_TODO_ENTITY.getDueAt(), todo.dueAt());
    }

    @Test
    void getByIdNotFound() {
        //given
        //when
        ThrowableProblem notFound = assertThrows(ThrowableProblem.class, () -> tested.getById(3L));
        //then
        Assertions.assertEquals(Status.NOT_FOUND, notFound.getStatus());
    }

    @Test
    void markAsDone() {
        //given
        when(todoRepository.findById(VALID_ID)).thenReturn(Optional.of(VALID_TODO_ENTITY));
        //when
        tested.markAsDone(VALID_ID);
        //then
        verify(todoRepository, times(1)).save(any(TodoEntity.class));
    }

    @Test
    void markAsDonePastDue() {
        //given
        when(todoRepository.findById(PAST_DUE_ID)).thenReturn(Optional.of(PAST_DUE_VALID_TODO_ENTITY));
        //when
        ThrowableProblem pastDue = assertThrows(ThrowableProblem.class, () -> tested.markAsDone(PAST_DUE_ID));
        //then
        Assertions.assertEquals(Status.CONFLICT, pastDue.getStatus());
    }

    @Test
    void markAsDoneNotFound() {
        //given
        //when
        ThrowableProblem notFound = assertThrows(ThrowableProblem.class, () -> tested.markAsDone(3L));
        //then
        Assertions.assertEquals(Status.NOT_FOUND, notFound.getStatus());
    }

    @Test
    void markAsNotDone() {
        //given
        when(todoRepository.findById(VALID_ID)).thenReturn(Optional.of(VALID_TODO_ENTITY));
        //when
        tested.markAsNotDone(VALID_ID);
        //then
        verify(todoRepository, times(1)).save(any(TodoEntity.class));
    }

    @Test
    void markAsNotDonePastDue() {
        //given
        when(todoRepository.findById(PAST_DUE_ID)).thenReturn(Optional.of(PAST_DUE_VALID_TODO_ENTITY));
        //when
        ThrowableProblem pastDue = assertThrows(ThrowableProblem.class, () -> tested.markAsNotDone(PAST_DUE_ID));
        //then
        Assertions.assertEquals(Status.CONFLICT, pastDue.getStatus());
    }

    @Test
    void markAsNotDoneNotFound() {
        //given
        //when
        ThrowableProblem notFound = assertThrows(ThrowableProblem.class, () -> tested.markAsNotDone(3L));
        //then
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
