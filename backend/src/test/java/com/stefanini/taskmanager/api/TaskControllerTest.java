package com.stefanini.taskmanager.api;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stefanini.taskmanager.application.TaskService;
import com.stefanini.taskmanager.application.dto.CreateTaskRequest;
import com.stefanini.taskmanager.application.dto.TaskResponse;
import com.stefanini.taskmanager.application.dto.UpdateTaskRequest;
import com.stefanini.taskmanager.application.exception.TaskNotFoundException;
import com.stefanini.taskmanager.domain.TaskStatus;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    private TaskResponse sampleResponse() {
        return new TaskResponse(1L, "Estudar", "desc", TaskStatus.PENDENTE, OffsetDateTime.now());
    }

    @Test
    void create_deveRetornar201ComLocation() throws Exception {
        when(taskService.create(any(CreateTaskRequest.class))).thenReturn(sampleResponse());
        CreateTaskRequest request = new CreateTaskRequest("Estudar", "desc", null);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.titulo", is("Estudar")));
    }

    @Test
    void create_comTituloEmBranco_deveRetornar400() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest("", "desc", null);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.fieldErrors[0].field", is("titulo")));
    }

    @Test
    void findAll_deveRetornarLista() throws Exception {
        when(taskService.findAll()).thenReturn(List.of(sampleResponse()));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void findById_quandoNaoExiste_deveRetornar404() throws Exception {
        when(taskService.findById(99L)).thenThrow(new TaskNotFoundException(99L));

        mockMvc.perform(get("/api/tasks/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    void update_deveRetornar200() throws Exception {
        TaskResponse updated = new TaskResponse(1L, "Novo", "desc", TaskStatus.CONCLUIDO, OffsetDateTime.now());
        when(taskService.update(eq(1L), any(UpdateTaskRequest.class))).thenReturn(updated);
        UpdateTaskRequest request = new UpdateTaskRequest("Novo", "desc", TaskStatus.CONCLUIDO);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("CONCLUIDO")));
    }

    @Test
    void delete_deveRetornar204() throws Exception {
        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());

        verify(taskService).delete(1L);
    }

    @Test
    void delete_quandoNaoExiste_deveRetornar404() throws Exception {
        doThrow(new TaskNotFoundException(99L)).when(taskService).delete(99L);

        mockMvc.perform(delete("/api/tasks/99"))
                .andExpect(status().isNotFound());
    }
}
