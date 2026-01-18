import { test as teardown } from "@playwright/test";
import mysql from "mysql2/promise";
import { test as setup, devices } from "@playwright/test";

setup.use({
  ...devices["Pixel 5"],
});

teardown("delete database, wipe mailpit", async ({ page }) => {
  console.log("---");
  console.log("deleting test database...");
  console.log("---");

  // Delete the database
  const conn = await mysql.createConnection({
    host: "localhost",
    port: 3306,
    user: process.env.MARIADB_USERNAME,
    password: process.env.MARIADB_PASSWORD,
    database: "online_bookstore",
  });

  await conn.execute("DELETE FROM users_roles");
  await conn.execute("DELETE FROM users_books");
  await conn.execute("DELETE FROM books");
  await conn.execute("DELETE FROM users");
  await conn.end();

  // Remove existing emails
  await page.goto("http://localhost:8025/");
  await page.getByRole("button").nth(1).tap();

  const deleteAllButton = page.getByRole("button", { name: " Delete all" });

  // Check if deletion button can be clicked. This is so it would only delete if there is something
  // to delete
  if (await deleteAllButton.isEnabled()) {
    await page.getByRole("button", { name: " Delete all" }).tap();
    await page.getByRole("button", { name: "Delete", exact: true }).tap();
  }
});
