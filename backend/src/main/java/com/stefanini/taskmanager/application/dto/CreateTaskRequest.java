package com.stefanini.taskmanager.application.dto;

import com.stefanini.taskmanager.domain.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTaskRequest(

        @NotBlank(message = "titulo é obrigatório")
        @Size(max = 150, message = "titulo deve ter no máximo 150 caracteres")
        String titulo,

        @Size(max = 1000, message = "descricao deve ter no máximo 1000 caracteres")
        String descricao,

        TaskStatus status
) {
}
