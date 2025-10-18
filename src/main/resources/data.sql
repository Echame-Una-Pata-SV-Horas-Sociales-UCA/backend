INSERT INTO public.roles(id, name, description) VALUES('ADMIN', 'administrador', 'Persona con los permisos mas altos para administrar todo el servicio y los participantes dentro de el')
    ON CONFLICT (id) DO UPDATE SET name = excluded.name;

INSERT INTO public.roles(id, name, description) VALUES('OPDR', 'operador', 'Persona con los permisos adecuados para realizar operaciones de nivel medio dentro del sistema')
    ON CONFLICT (id) DO UPDATE SET name = excluded.name;

INSERT INTO public.roles(id, name, description) VALUES('USER', 'usuarios', 'Persona con los permisos mas bajos del sistema')
    ON CONFLICT (id) DO UPDATE SET name = excluded.name;