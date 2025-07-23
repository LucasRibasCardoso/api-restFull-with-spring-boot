CREATE TABLE tb_person (
  id SERIAL PRIMARY KEY,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  cpf VARCHAR(100) NOT NULL UNIQUE,
  gender VARCHAR(6) NOT NULL CHECK (gender IN ('M','F'))
);
