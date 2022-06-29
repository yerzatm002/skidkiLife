ALTER TABLE establishments
ALTER COLUMN created_date TYPE TIMESTAMP;

ALTER table sales
RENAME COLUMN createddate TO created_date;

ALTER TABLE sales
ALTER COLUMN created_date TYPE TIMESTAMP;