package com.stefanini.taskmanager.application.exception;

/**
 * Lançada quando uma tarefa não é encontrada pelo identificador informado.
 */
public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long id) {
        super("Tarefa não encontrada para o id " + id);
    }
}
