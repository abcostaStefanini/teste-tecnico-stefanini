import { STATUS_LABELS, type Task } from '../types/task';

interface Props {
  tasks: Task[];
}

const statusStyles: Record<Task['status'], string> = {
  PENDENTE: 'bg-yellow-100 text-yellow-800',
  EM_ANDAMENTO: 'bg-blue-100 text-blue-800',
  CONCLUIDO: 'bg-green-100 text-green-800',
};

function formatDate(value: string): string {
  return new Date(value).toLocaleString('pt-BR');
}

export default function TaskList({ tasks }: Props) {
  if (tasks.length === 0) {
    return (
      <p className="text-center text-gray-500 py-8">
        Nenhuma tarefa cadastrada ainda.
      </p>
    );
  }

  return (
    <ul className="space-y-3">
      {tasks.map((task) => (
        <li
          key={task.id}
          className="border border-gray-200 rounded-lg p-4 bg-white shadow-sm"
        >
          <div className="flex items-start justify-between gap-3">
            <div>
              <h3 className="font-semibold text-gray-900">{task.titulo}</h3>
              {task.descricao && (
                <p className="text-sm text-gray-600 mt-1">{task.descricao}</p>
              )}
              <p className="text-xs text-gray-400 mt-2">
                Criada em {formatDate(task.dataCriacao)}
              </p>
            </div>
            <span
              className={`text-xs font-medium px-2.5 py-1 rounded-full whitespace-nowrap ${statusStyles[task.status]}`}
            >
              {STATUS_LABELS[task.status]}
            </span>
          </div>
        </li>
      ))}
    </ul>
  );
}
