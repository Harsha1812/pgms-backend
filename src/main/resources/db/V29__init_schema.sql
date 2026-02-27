ALTER TABLE users
DROP CONSTRAINT chk_user_role;

ALTER TABLE users
    ADD CONSTRAINT chk_user_role
        CHECK (
            role IN (
                     'OWNER_ADMIN',
                     'BRANCH_MANAGER',
                     'STAFF',
                     'SUPER_ADMIN'
                )
            );

INSERT INTO owners (
    id,
    email,
    password_hash,
    full_name,
    phone,
    is_active,
    created_at,
    updated_at
)
VALUES (
           '00000000-0000-0000-0000-000000000001',
           'system@pgms.internal',
           'SYSTEM',
           'SYSTEM_OWNER',
           NULL,
           true,
           now(),
           now()
       );


INSERT INTO users (
    id,
    owner_id,
    email,
    password_hash,
    full_name,
    role,
    is_active,
    created_by,
    updated_by,
    created_at,
    updated_at
)
VALUES (
           '00000000-0000-0000-0000-000000000002',
           '00000000-0000-0000-0000-000000000001',
           'system@pgms.internal',
           'SYSTEM',
           'SYSTEM_USER',
           'SUPER_ADMIN',
           true,
           '00000000-0000-0000-0000-000000000002',
           '00000000-0000-0000-0000-000000000002',
           now(),
           now()
       );