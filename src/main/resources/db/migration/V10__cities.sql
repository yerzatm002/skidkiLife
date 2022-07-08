create table if not exists cities (
   id bigint NOT NULL,
   name character varying(255)
);

ALTER TABLE cities ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME cities_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);

ALTER TABLE establishments
    ADD city bigint;