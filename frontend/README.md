# Frontend — Task Manager

SPA em **React + TypeScript + Vite + Tailwind CSS** para listar e criar tarefas,
consumindo a API REST do backend (`/api/tasks`).

## Estrutura

```
src/
├── api/taskApi.ts          Cliente HTTP tipado (axios)
├── components/
│   ├── TaskForm.tsx        Formulário de criação (POST /tasks)
│   └── TaskList.tsx        Listagem de tarefas (GET /tasks)
├── types/task.ts           Tipos compartilhados (Task, TaskStatus, DTOs)
├── App.tsx                 Composição da tela + estados de loading/erro
├── main.tsx                Entry point
└── index.css               Diretivas Tailwind
```

## Scripts

```bash
npm install      # instala dependências
npm run dev      # servidor de desenvolvimento (http://localhost:5173)
npm run build    # type-check + build de produção
npm run preview  # serve o build de produção
```

## Configuração de API

- Em desenvolvimento, o Vite faz proxy de `/api` para `http://localhost:8080`
  (ver `vite.config.ts`), evitando CORS.
- A base pode ser sobrescrita via `VITE_API_BASE_URL` (ver `.env.example`).
