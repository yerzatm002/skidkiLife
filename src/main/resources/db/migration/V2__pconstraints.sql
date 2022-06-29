Alter table only establishments
    ADD CONSTRAINT establishments_pkey PRIMARY KEY (id);

ALTER TABLE ONLY roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);

ALTER TABLE ONLY sales
    ADD CONSTRAINT sales_pkey PRIMARY KEY (id);

ALTER TABLE ONLY work_schedules
    ADD CONSTRAINT work_schedules_pkey PRIMARY KEY (id);

ALTER TABLE ONLY establishment_work_schedules
    ADD CONSTRAINT establishment_work_schedules_pkey PRIMARY KEY (establishment_id, day_id);

ALTER TABLE ONLY skidki_employees
    ADD CONSTRAINT skidki_employees_pkey PRIMARY KEY (id);

ALTER TABLE ONLY sms_verification
    ADD CONSTRAINT sms_verification_pkey PRIMARY KEY (id);