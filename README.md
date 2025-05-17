# Book recommendation generator

Generate new books to read using artificial intelligence.

**Features**:

* Search for new books to read, based on a query
* Save a book you find interesting for later reference
* Dislike a book from being recommended again
* Track how many books you have saved to your list, for later reading

## Requirements

* JDK 17+
* Node 20+
* MariaDB 10.11+
* OpenAI API

## How to run

### Backend

Set `SPRING_AI_OPENAI_API_KEY` environment variable as your OpenAI API key.

Set `${MARIADB_USERNAME}` environment variable as your database username, and `${MARIADB_PASSWORD}` as password.

You will need to create a new database in MariaDB, named `online_bookstore`. The database must be filled with this DDL:

```SQL
CREATE TABLE `roles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `books` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`title`,`user_id`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);

CREATE TABLE `users_roles` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
);

INSERT INTO roles (name)
VALUES ('ROLE_USER'), ('ROLE_ADMIN');
```

Clone the the repository. `cd` into `online-bookstore` folder, then into `backend`.

In the `backend` folder, open a terminal. Execute `./mvnw spring-boot:run` command to run the backend.

### Frontend

`cd` into `frontend` folder, and open a terminal there. You must first install the dependencies - run `npm install`.

Now run the frontend, using the command `npm run dev`.

Open a browser and navigate to `http://localhost:5173`. Enjoy!