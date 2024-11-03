CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS "user" (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  first_name VARCHAR(20) NOT NULL,
  last_name VARCHAR(20) NOT NULL,
  email VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(120) NOT NULL
);

CREATE TABLE IF NOT EXISTS role (
  id SERIAL PRIMARY KEY,
  name VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS user_roles (
  user_id UUID,
  role_id INT,
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (user_id) REFERENCES "user" (id),
  FOREIGN KEY (role_id) REFERENCES role (id)
);

DO $$
BEGIN
   IF NOT EXISTS (SELECT 1 FROM role) THEN
      INSERT INTO role (name)
      VALUES
        ('ROLE_USER'),
        ('ROLE_VIEWER'),
        ('ROLE_MODERATOR'),
        ('ROLE_MANAGER');
   END IF;
END $$;


CREATE TABLE IF NOT EXISTS kupoprodajni_ugovor (
    id SERIAL PRIMARY KEY,
    kupac TEXT NOT NULL,
    broj_ugovora TEXT NOT NULL,
    datum_akontacije DATE NOT NULL,
    rok_isporuke DATE NOT NULL,
    status VARCHAR (50) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS artikl (
    id SERIAL PRIMARY KEY,
    naziv TEXT NOT NULL,
    dobavljac TEXT,
    kolicina INT NOT NULL,
    status VARCHAR (50) NOT NULL,
    ugovor_id INT REFERENCES kupoprodajni_ugovor(id)
);

WITH inserted_user AS (
  INSERT INTO public.user (id, email, password, first_name, last_name)
  VALUES ('31f91b36-dd1f-480b-b3bb-5dc55bc17e75',
          'artem.java.sirobaba@omegasoft.com',
          '$2a$12$yIuciay7JmoEgsuxPTjB9OHVV2WfGkTGJ.FkjyQmRGtB2QyJBjp72',
          'Artem',
          'Sirobaba'),
           ('223650db-cb29-4e04-8be1-ffb56675abdf',
           'artem.test.java.sirobaba@omegasoft.com.co',
          '$2a$10$MUZhmdJTTtFiLQZe6KGyyefErddWcnVPhng7e76ZQitKop9b86Egq',
          'Artem',
          'Sirobaba')
  RETURNING id
)

INSERT INTO user_roles (user_id, role_id)
SELECT id, 4
FROM inserted_user;

INSERT INTO kupoprodajni_ugovor (id, kupac, broj_ugovora, datum_akontacije, rok_isporuke, status, is_active, is_deleted) VALUES
(1, 'Petra Kranjčar', '1/2024', '2024-01-04', '2024-04-20', 'KREIRANO', TRUE, FALSE),
(2, 'Franko Kasun', '2/2024', '2024-03-01', '2024-05-01', 'ISPORUCENO', FALSE, FALSE),
(3, 'Stjepan Babić', '3/2024', '2024-03-03', '2024-04-15', 'NARUCENO', TRUE, FALSE),
(4, 'Tia Janković', '4/2024', '2024-03-14', '2024-08-13', 'KREIRANO', TRUE, FALSE);

INSERT INTO artikl (id, naziv, dobavljac, kolicina, status, ugovor_id) VALUES
(101, 'Perilica posuđa ugradbena Electrolux EEA27200L', 'Sancta Domenica', 1, 'KREIRANO', 1),
(102, 'Perilica posuđa ugradbena Electrolux EEA27200L', 'Sancta Domenica', 1, 'KREIRANO', 4),
(103, 'Napa ugradbena Gorenje TH60E3X', 'Sancta Domenica', 1, 'NARUCENO', 3),
(104, 'Ploča ugradbena kombinirana Gorenje GCE691BSC', 'Bijela tehnika', 1, 'ISPORUCENO', 2);