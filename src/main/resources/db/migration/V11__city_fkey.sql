ALTER TABLE ONLY cities
    ADD CONSTRAINT cities_pkey PRIMARY KEY (id);

ALTER TABLE ONLY establishments
    ADD CONSTRAINT establishmentcityfkkey FOREIGN KEY (city) REFERENCES cities(id);

insert into cities(name)
values ('Алматы');
insert into cities(name)
values ('Нур-Султан');
insert into cities(name)
values ('Шымкент');
insert into cities(name)
values ('Актобе');
insert into cities(name)
values ('Караганда');
insert into cities(name)
values ('Атырау');
insert into cities(name)
values ('Тараз');
insert into cities(name)
values ('Павлодар');
insert into cities(name)
values ('Семей');
insert into cities(name)
values ('Усть-Каменогорск');
insert into cities(name)
values ('Кызылорда');
insert into cities(name)
values ('Уральск');
insert into cities(name)
values ('Костанай');
insert into cities(name)
values ('Петропавлоск');
insert into cities(name)
values ('Актау');
insert into cities(name)
values ('Темиртау');
insert into cities(name)
values ('Туркестан');
insert into cities(name)
values ('Талдыкорган');
insert into cities(name)
values ('Кокшетау');
insert into cities(name)
values ('Жанаозен');
insert into cities(name)
values ('Экибастуз');
insert into cities(name)
values ('Рудный');