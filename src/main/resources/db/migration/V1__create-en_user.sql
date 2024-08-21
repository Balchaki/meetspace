CREATE TABLE en_user (
                         seq_user SERIAL PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         email VARCHAR(100) UNIQUE NOT NULL,
                         password VARCHAR(255) NOT NULL,
                         is_admin BOOLEAN DEFAULT FALSE
);
