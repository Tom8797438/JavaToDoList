-- Indexes to speed up filtering by done status.
CREATE INDEX IF NOT EXISTS idx_task_done ON public.tasks(done);
