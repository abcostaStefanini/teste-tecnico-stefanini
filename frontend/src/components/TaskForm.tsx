import { useState } from 'react';
import { STATUS_LABELS, type CreateTaskRequest, type TaskStatus } from '../types/task';

interface Props {
  onSubmit: (payload: CreateTaskRequest) => Promise<void>;
  submitting: boolean;
}

const STATUS_OPTIONS: TaskStatus[] = ['PENDENTE', 'EM_ANDAMENTO', 'CONCLUIDO'];

export default function TaskForm({ onSubmit, submitting }: Props) {
  const [titulo, setTitulo] = useState('');
  const [descricao, setDescricao] = useState('');
  const [status, setStatus] = useState<TaskStatus>('PENDENTE');
  const [error, setError] = useState<string | null>(null);

  async function handleSubmit(event: React.FormEvent) {
    event.preventDefault();
    setError(null);

    if (!titulo.trim()) {
      setError('O título é obrigatório.');
      return;
    }

    await onSubmit({
      titulo: titulo.trim(),
      descricao: descricao.trim() || undefined,
      status,
    });

    setTitulo('');
    setDescricao('');
    setStatus('PENDENTE');
  }

  return (
    <form
      onSubmit={handleSubmit}
      className="bg-white border border-gray-200 rounded-lg p-5 shadow-sm space-y-4"
    >
      <h2 className="text-lg font-semibold text-gray-900">Nova tarefa</h2>

      <div>
        <label htmlFor="titulo" className="block text-sm font-medium text-gray-700">
          Título
        </label>
        <input
          id="titulo"
          type="text"
          value={titulo}
          maxLength={150}
          onChange={(e) => setTitulo(e.target.value)}
          className="mt-1 w-full rounded-md border border-gray-300 px-3 py-2 focus:border-blue-500 focus:outline-none focus:ring-1 focus:ring-blue-500"
          placeholder="Ex.: Estudar Spring Boot"
        />
      </div>

      <div>
        <label htmlFor="descricao" className="block text-sm font-medium text-gray-700">
          Descrição
        </label>
        <textarea
          id="descricao"
          value={descricao}
          maxLength={1000}
          rows={3}
          onChange={(e) => setDescricao(e.target.value)}
          className="mt-1 w-full rounded-md border border-gray-300 px-3 py-2 focus:border-blue-500 focus:outline-none focus:ring-1 focus:ring-blue-500"
          placeholder="Detalhes da tarefa (opcional)"
        />
      </div>

      <div>
        <label htmlFor="status" className="block text-sm font-medium text-gray-700">
          Status
        </label>
        <select
          id="status"
          value={status}
          onChange={(e) => setStatus(e.target.value as TaskStatus)}
          className="mt-1 w-full rounded-md border border-gray-300 px-3 py-2 focus:border-blue-500 focus:outline-none focus:ring-1 focus:ring-blue-500"
        >
          {STATUS_OPTIONS.map((option) => (
            <option key={option} value={option}>
              {STATUS_LABELS[option]}
            </option>
          ))}
        </select>
      </div>

      {error && <p className="text-sm text-red-600">{error}</p>}

      <button
        type="submit"
        disabled={submitting}
        className="w-full rounded-md bg-blue-600 px-4 py-2 font-medium text-white hover:bg-blue-700 disabled:cursor-not-allowed disabled:opacity-60"
      >
        {submitting ? 'Salvando...' : 'Criar tarefa'}
      </button>
    </form>
  );
}
