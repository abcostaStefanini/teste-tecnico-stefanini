import { useCallback, useEffect, useState } from 'react';
import TaskForm from './components/TaskForm';
import TaskList from './components/TaskList';
import { createTask, listTasks } from './api/taskApi';
import type { CreateTaskRequest, Task } from './types/task';

export default function App() {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchTasks = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      setTasks(await listTasks());
    } catch {
      setError('Não foi possível carregar as tarefas. Verifique se a API está disponível.');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    void fetchTasks();
  }, [fetchTasks]);

  async function handleCreate(payload: CreateTaskRequest) {
    setSubmitting(true);
    setError(null);
    try {
      const created = await createTask(payload);
      setTasks((prev) => [...prev, created]);
    } catch {
      setError('Não foi possível criar a tarefa. Tente novamente.');
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-white border-b border-gray-200">
        <div className="mx-auto max-w-5xl px-4 py-5">
          <h1 className="text-2xl font-bold text-gray-900">Task Manager</h1>
          <p className="text-sm text-gray-500">Gerenciador de tarefas event-driven</p>
        </div>
      </header>

      <main className="mx-auto max-w-5xl px-4 py-8 grid gap-8 md:grid-cols-[1fr_2fr]">
        <section>
          <TaskForm onSubmit={handleCreate} submitting={submitting} />
        </section>

        <section>
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-lg font-semibold text-gray-900">Tarefas</h2>
            <button
              onClick={() => void fetchTasks()}
              className="text-sm text-blue-600 hover:underline"
            >
              Atualizar
            </button>
          </div>

          {error && (
            <div className="mb-4 rounded-md bg-red-50 border border-red-200 px-4 py-3 text-sm text-red-700">
              {error}
            </div>
          )}

          {loading ? (
            <p className="text-center text-gray-500 py-8">Carregando tarefas...</p>
          ) : (
            <TaskList tasks={tasks} />
          )}
        </section>
      </main>
    </div>
  );
}
