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
