-- TODO: update readme file with this
-- TODO: I think the length of columns should match reality. As well as, check if all unique is good. Check if I am able to register
-- with these new changes...
-- Is it bad I already pushed the db with unique key codes to repository?!
-- Wait a minute... my db design is sus. Now, a user can have many books, but one book belongs to one user?!

CREATE TABLE roles (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL
);

INSERT INTO roles (name)
VALUES ('ROLE_USER'), ('ROLE_ADMIN');

CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL
);

CREATE TABLE users_roles (
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE books (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE users_books (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  book_id BIGINT NOT NULL,
  UNIQUE KEY (user_id, book_id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (book_id) REFERENCES books(id)
);