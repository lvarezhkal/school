ALTER TABLE Student ADD Constraint  age_constraint CHECK (age > 15);
alter table student add constraint name_unique Unique (name);
alter table student ALTER COLUMN name SET NOT NULL;
ALTER TABLE faculty ADD CONSTRAINT name_color_unique UNIQUE (name, color);
alter TABLE student alter column age set DEFAULT 20;