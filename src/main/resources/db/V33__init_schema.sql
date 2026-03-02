ALTER TABLE public.owners
    ADD COLUMN email_hash varchar(255);

CREATE UNIQUE INDEX uk_owner_email_hash
    ON public.owners(email_hash);