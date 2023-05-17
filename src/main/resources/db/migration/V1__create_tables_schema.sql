
CREATE TABLE IF NOT EXISTS wallet (
  id SERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  balance DECIMAL(19, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
  id SERIAL PRIMARY KEY,
  first_name VARCHAR(255) NOT NULL,
  surname VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS transaction (
  id SERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  amount DECIMAL(19, 2) NOT NULL,
  transaction_type VARCHAR(255) NOT NULL,
  status VARCHAR(255) NOT NULL,
  created_at timestamp without time zone NOT NULL
);




