import axios from 'axios';
import type { CreateTaskRequest, Task } from '../types/task';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? '/api',
  headers: { 'Content-Type': 'application/json' },
});

export async function listTasks(): Promise<Task[]> {
  const { data } = await api.get<Task[]>('/tasks');
  return data;
}

export async function createTask(payload: CreateTaskRequest): Promise<Task> {
  const { data } = await api.post<Task>('/tasks', payload);
  return data;
}
