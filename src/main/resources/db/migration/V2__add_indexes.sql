-- V2__add_indexes.sql
CREATE INDEX IF NOT EXISTS idx_task_done ON public.tasks(done);
