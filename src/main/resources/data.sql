INSERT INTO public.roles(id, name, description, created_at, updated_at)
VALUES ('ADMIN', 'administrador', 'Persona con los permisos más altos para administrar todo el servicio y los participantes dentro del sistema', NOW(), NOW())
    ON CONFLICT (id) DO UPDATE
                            SET name = excluded.name,
                            description = excluded.description,
                            updated_at = NOW();

INSERT INTO public.roles(id, name, description, created_at, updated_at)
VALUES ('OPDR', 'operador', 'Persona con los permisos adecuados para realizar operaciones de nivel medio dentro del sistema', NOW(), NOW())
    ON CONFLICT (id) DO UPDATE
                            SET name = excluded.name,
                            description = excluded.description,
                            updated_at = NOW();

INSERT INTO public.roles(id, name, description, created_at, updated_at)
VALUES ('USER', 'usuarios', 'Persona con los permisos más bajos del sistema', NOW(), NOW())
    ON CONFLICT (id) DO UPDATE
                            SET name = excluded.name,
                            description = excluded.description,
                            updated_at = NOW();
