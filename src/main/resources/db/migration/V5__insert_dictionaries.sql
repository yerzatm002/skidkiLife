INSERT INTO skidki_employees(name, employee_created_at, major, phone_number, password, role_id)
VALUES ('SUPER ADMIN', '2022-02-08', 'Супер Админ', '+77015310007', '9234666DCC951C274339903FBABE5F72', 2);

INSERT INTO work_schedules(day)
VALUES ('Понедельник');
INSERT INTO work_schedules(day)
VALUES ('Вторник');
INSERT INTO work_schedules(day)
VALUES ('Среда');
INSERT INTO work_schedules(day)
VALUES ('Четверг');
INSERT INTO work_schedules(day)
VALUES ('Пятница');
INSERT INTO work_schedules(day)
VALUES ('Суббота');
INSERT INTO work_schedules(day)
VALUES ('Воскресенье');

insert into establishments(phone_number, password, establishment_name, address, longitude, latitude, establishment_type, description,from_work_schedule, to_work_schedule, is_activated, is_enabled, role_id)
values ('+77778127067', '9234666DCC951C274339903FBABE5F72', 'SYSADMIN', 'SYSADMIN', '0', '0', 'SYSADMIN', 'SYSADMIN', '09:00:00', '18:00:00', true, true, 2);