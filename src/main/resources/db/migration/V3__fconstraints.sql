ALTER TABLE ONLY user_roles
    ADD CONSTRAINT fkh8ciramu9cc9q3qcqiv4ue8a6 FOREIGN KEY (role_id) REFERENCES roles(id);

ALTER TABLE ONLY user_roles
    ADD CONSTRAINT fkhfh9dx7w3ubf1co1vdev94g3f FOREIGN KEY (user_id) REFERENCES establishments(id);

ALTER TABLE ONLY establishment_work_schedules
    ADD CONSTRAINT establishmentiddsadsafkkey FOREIGN KEY (establishment_id) REFERENCES establishments(id) ON DELETE CASCADE;

ALTER TABLE ONLY establishment_work_schedules
    ADD CONSTRAINT establishmentworkschedulesdayidfkkey FOREIGN KEY (day_id) REFERENCES work_schedules(id);

ALTER TABLE ONLY sales
    ADD CONSTRAINT fkss2obdlksrbsmc28pe4q7kq24 FOREIGN KEY (establishment_id) REFERENCES establishments(id) ON DELETE CASCADE;

-- ALTER TABLE ONLY establishments
--     ADD CONSTRAINT establishmentcityfkkey FOREIGN KEY (city) REFERENCES cities(id);
