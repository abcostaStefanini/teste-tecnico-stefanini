package com.stefanini.taskmanager.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.stefanini.taskmanager.application.dto.CreateTaskRequest;
import com.stefanini.taskmanager.application.dto.TaskResponse;
import com.stefanini.taskmanager.application.dto.UpdateTaskRequest;
import com.stefanini.taskmanager.application.exception.TaskNotFoundException;
import com.stefanini.taskmanager.domain.Task;
import com.stefanini.taskmanager.domain.TaskStatus;
import com.stefanini.taskmanager.domain.event.TaskCreatedEvent;
import com.stefanini.taskmanager.domain.event.TaskEvent;
import com.stefanini.taskmanager.domain.event.TaskUpdatedEvent;
import com.stefanini.taskmanager.infrastructure.messaging.TaskEventProducer;
import com.stefanini.taskmanager.infrastructure.persistence.TaskRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskEventProducer eventProducer;

    @InjectMocks
    private TaskService taskService;

    private Task buildTask(Long id, String titulo, TaskStatus status) {
        return Task.builder()
                .id(id)
                .titulo(titulo)
                .descricao("descricao")
                .status(status)
                .dataCriacao(OffsetDateTime.now())
                .build();
    }

    @Test
    void create_devePersistirEPublicarEventoCreated() {
        CreateTaskRequest request = new CreateTaskRequest("Estudar Kafka", "desc", null);
        Task saved = buildTask(1L, "Estudar Kafka", TaskStatus.PENDENTE);
        when(taskRepository.save(any(Task.class))).thenReturn(saved);

        TaskResponse response = taskService.create(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.status()).isEqualTo(TaskStatus.PENDENTE);

        ArgumentCaptor<TaskEvent> eventCaptor = ArgumentCaptor.forClass(TaskEvent.class);
        verify(eventProducer).publish(eventCaptor.capture());
        assertThat(eventCaptor.getValue()).isInstanceOf(TaskCreatedEvent.class);
        assertThat(eventCaptor.getValue().taskId()).isEqualTo(1L);
    }

    @Test
    void create_quandoStatusInformado_deveUsarStatusDoRequest() {
        CreateTaskRequest request = new CreateTaskRequest("Tarefa", "desc", TaskStatus.EM_ANDAMENTO);
        Task saved = buildTask(2L, "Tarefa", TaskStatus.EM_ANDAMENTO);
        when(taskRepository.save(any(Task.class))).thenReturn(saved);

        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
        taskService.create(request);

        verify(taskRepository).save(taskCaptor.capture());
        assertThat(taskCaptor.getValue().getStatus()).isEqualTo(TaskStatus.EM_ANDAMENTO);
    }

    @Test
    void findAll_deveRetornarListaMapeada() {
        when(taskRepository.findAll()).thenReturn(List.of(
                buildTask(1L, "A", TaskStatus.PENDENTE),
                buildTask(2L, "B", TaskStatus.CONCLUIDO)));

        List<TaskResponse> result = taskService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(TaskResponse::titulo).containsExactly("A", "B");
    }

    @Test
    void findById_quandoExiste_deveRetornar() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(buildTask(1L, "A", TaskStatus.PENDENTE)));

        TaskResponse response = taskService.findById(1L);

        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    void findById_quandoNaoExiste_deveLancarExcecao() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.findById(99L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void update_deveAtualizarEPublicarEventoUpdated() {
        Task existing = buildTask(1L, "Antigo", TaskStatus.PENDENTE);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        UpdateTaskRequest request = new UpdateTaskRequest("Novo", "nova desc", TaskStatus.CONCLUIDO);
        TaskResponse response = taskService.update(1L, request);

        assertThat(response.titulo()).isEqualTo("Novo");
        assertThat(response.status()).isEqualTo(TaskStatus.CONCLUIDO);

        ArgumentCaptor<TaskEvent> eventCaptor = ArgumentCaptor.forClass(TaskEvent.class);
        verify(eventProducer).publish(eventCaptor.capture());
        assertThat(eventCaptor.getValue()).isInstanceOf(TaskUpdatedEvent.class);
    }

    @Test
    void update_quandoNaoExiste_deveLancarExcecaoENaoPublicar() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
        UpdateTaskRequest request = new UpdateTaskRequest("X", "y", TaskStatus.PENDENTE);

        assertThatThrownBy(() -> taskService.update(99L, request))
                .isInstanceOf(TaskNotFoundException.class);

        verify(eventProducer, never()).publish(any());
    }

    @Test
    void delete_quandoExiste_deveRemover() {
        Task existing = buildTask(1L, "A", TaskStatus.PENDENTE);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existing));

        taskService.delete(1L);

        verify(taskRepository).delete(existing);
    }

    @Test
    void delete_quandoNaoExiste_deveLancarExcecao() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.delete(99L))
                .isInstanceOf(TaskNotFoundException.class);

        verify(taskRepository, never()).delete(any());
    }
}
