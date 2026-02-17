SELECT * FROM public.users
ORDER BY user_id ASC 

insert into public.users(username,password)
	values('admin','admin123');


create EXTENSION IF NOT EXISTS pgcrypto;
INSERT INTO public.users (username, password)
VALUES ('admin', crypt('admin123', gen_salt('bf')));

SELECT username, password FROM public.users WHERE username='admin';
