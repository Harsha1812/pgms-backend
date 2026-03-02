ALTER TABLE public.users
    ADD COLUMN status varchar(30) NOT NULL DEFAULT 'PENDING';

ALTER TABLE public.users
    ADD COLUMN approved boolean NOT NULL DEFAULT false;

ALTER TABLE public.users
    ADD COLUMN email_verified boolean NOT NULL DEFAULT false;

ALTER TABLE public.users
    ADD COLUMN email_hash varchar(255);

ALTER TABLE public.users
    ADD COLUMN tenant_id uuid NULL;

ALTER TABLE public.users
    ADD CONSTRAINT fk_user_tenant
        FOREIGN KEY (tenant_id) REFERENCES public.tenants(id);

ALTER TABLE public.users
DROP CONSTRAINT chk_user_role;

ALTER TABLE public.users
    ADD CONSTRAINT chk_user_role
        CHECK (role IN (
                        'OWNER_ADMIN',
                        'BRANCH_MANAGER',
                        'STAFF',
                        'SUPER_ADMIN',
                        'TENANT'
            ));

ALTER TABLE public.users
    ADD CONSTRAINT chk_user_status
        CHECK (status IN (
                          'PENDING',
                          'ACTIVE',
                          'SUSPENDED',
                          'DELETED'
            ));

CREATE UNIQUE INDEX uk_user_email_hash_per_owner
    ON public.users(owner_id, email_hash);

CREATE TABLE public.user_branches (
                                      user_id uuid NOT NULL,
                                      branch_id uuid NOT NULL,
                                      PRIMARY KEY (user_id, branch_id),
                                      CONSTRAINT fk_user_branches_user
                                          FOREIGN KEY (user_id) REFERENCES public.users(id),
                                      CONSTRAINT fk_user_branches_branch
                                          FOREIGN KEY (branch_id) REFERENCES public.branches(id)
);

CREATE TABLE public.email_verification_tokens (
                                                  id uuid PRIMARY KEY,
                                                  user_id uuid NOT NULL,
                                                  token_hash varchar(255) NOT NULL,
                                                  expires_at timestamp NOT NULL,
                                                  used boolean DEFAULT false,
                                                  created_at timestamp NOT NULL,
                                                  CONSTRAINT fk_email_verification_user
                                                      FOREIGN KEY (user_id) REFERENCES public.users(id)
);

