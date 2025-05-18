import { test as teardown } from "@playwright/test";
import mysql from "mysql2/promise";

teardown("delete database", async ({}) => {
  console.log("---");
  console.log("deleting test database...");
  console.log("---");

  // Delete the database
  const conn = await mysql.createConnection({
    host: "localhost",
    port: 3307,
    user: process.env.MARIADB_USERNAME,
    password: process.env.MARIADB_PASSWORD,
    database: "online_bookstore_test_env",
  });

  await conn.execute("DELETE FROM users_roles");
  await conn.execute("DELETE FROM books");
  await conn.execute("DELETE FROM users");
  await conn.end();
});
