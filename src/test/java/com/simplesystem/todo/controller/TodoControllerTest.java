package com.simplesystem.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplesystem.todo.models.TodoCreationDto;
import com.simplesystem.todo.models.TodoModificationDto;
import com.simplesystem.todo.service.TodoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
class TodoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createTodo() throws Exception {
        // happy path
        TodoCreationDto todoCreationDto = new TodoCreationDto(OffsetDateTime.now().plus(1, ChronoUnit.DAYS), "test");
        mockMvc.perform(post("/todos").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(todoCreationDto))).andExpect(status().isOk());
    }

    @Test
    void createTodo_PastDueDate() throws Exception {
        TodoCreationDto pastDue = new TodoCreationDto(OffsetDateTime.now(), "test");
        mockMvc.perform(post("/todos").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(pastDue))).andExpect(status().isBadRequest());
    }

    @Test
    void createTodo_EmptyDescription() throws Exception {
        TodoCreationDto emptyDescription = new TodoCreationDto(OffsetDateTime.now().plus(1, ChronoUnit.DAYS), "");
        mockMvc.perform(post("/todos").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(emptyDescription))).andExpect(status().isBadRequest());
    }

    @Test
    void createTodo_EmptyDescriptionAndPastDueDate() throws Exception {
        TodoCreationDto emptyDescriptionAndPastDue = new TodoCreationDto(null, null);
        mockMvc.perform(post("/todos").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(emptyDescriptionAndPastDue))).andExpect(status().isBadRequest());
    }


    @Test
    void updateTodo() throws Exception {
        TodoModificationDto todoModificationDto = new TodoModificationDto( "test");
        mockMvc.perform(put("/todos/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(todoModificationDto))).andExpect(status().isOk());
    }

    @Test
    void updateTodo_EmptyDescription() throws Exception {
        TodoModificationDto emptyDescription = new TodoModificationDto( "");
        mockMvc.perform(put("/todos/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(emptyDescription))).andExpect(status().isBadRequest());
    }

    @Test
    void markTodoAsDone() throws Exception {
        mockMvc.perform(patch("/todos/1/done")).andExpect(status().isOk());
    }

    @Test
    void markTodoAsUndone() throws Exception {
        mockMvc.perform(patch("/todos/1/not-done")).andExpect(status().isOk());
    }

    @Test
    void getTodoById() throws Exception {
        mockMvc.perform(get("/todos/1")).andExpect(status().isOk());
    }

    @Test
    void getAllTodos() throws Exception {
        mockMvc.perform(get("/todos")).andExpect(status().isOk());
    }
}
