export type TaskStatus = 'PENDENTE' | 'EM_ANDAMENTO' | 'CONCLUIDO';

export interface Task {
  id: number;
  titulo: string;
  descricao: string | null;
  status: TaskStatus;
  dataCriacao: string;
}

export interface CreateTaskRequest {
  titulo: string;
  descricao?: string;
  status?: TaskStatus;
}

export const STATUS_LABELS: Record<TaskStatus, string> = {
  PENDENTE: 'Pendente',
  EM_ANDAMENTO: 'Em andamento',
  CONCLUIDO: 'Concluído',
};
