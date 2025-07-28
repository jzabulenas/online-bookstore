import { test, expect } from "@playwright/test";
import { v4 as uuidv4 } from "uuid";

test("should click like on a single generated book, and see it displayed in 'saved books'", async ({
  page,
}) => {
  const email = `antanas+${uuidv4()}@inbox.lt`;

  await page.goto("http://localhost:5173/");
  await page.getByRole("link", { name: "Sign up" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:" }).click();
  await page.getByRole("textbox", { name: "Password:" }).fill("12345678");
  await page.getByRole("button", { name: "Submit" }).click();
  await page.locator(".alert.alert-success.alert-dismissible").waitFor(); // Waits for success sign up message

  await page.getByRole("link", { name: "Log in" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:" }).click();
  await page.getByRole("textbox", { name: "Password:" }).fill("12345678");
  await page.getByRole("button", { name: "Submit" }).click();

  await page.getByRole("textbox", { name: "Input your book:" }).click();
  await page
    .getByRole("textbox", { name: "Input your book:" })
    .fill("The Merry Adventures of Robin Hood by Howard Pyle");
  await page.getByRole("button", { name: "Submit" }).click();
  const books = page.getByTestId("generated-books").locator("p"); // Selects all paragraphs in 'generated-books' test-id
  await books.nth(0).getByRole("img").click(); // Clicks on the like button of the first paragraph
  const bookTitle = await books.nth(0).innerText(); // Get the book title of previously clicked book
  await page.getByRole("link", { name: "Saved books" }).click();

  await expect(page).toHaveURL("http://localhost:5173/saved-books");
  await expect(page.getByText("Books you have saved")).toBeVisible();
  await expect(page.getByRole("heading")).toContainText("Books you have saved");
  await expect(page.getByText(bookTitle)).toBeVisible();
  // await expect(page.getByRole("paragraph")).toContainText(bookTitle);
  await expect(page.getByRole("main")).toMatchAriaSnapshot(`
    - main:
      - heading "Books you have saved" [level=1]
      - paragraph
    `);
});

test("should click like on two generated books, and see them displayed in 'saved books'", async ({
  page,
}) => {
  const email = `antanas+${uuidv4()}@inbox.lt`;

  await page.goto("http://localhost:5173/");
  await page.getByRole("link", { name: "Sign up" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:" }).click();
  await page.getByRole("textbox", { name: "Password:" }).fill("12345678");
  await page.getByRole("button", { name: "Submit" }).click();
  await page.locator(".alert.alert-success.alert-dismissible").waitFor(); // Waits for success sign up message

  await page.getByRole("link", { name: "Log in" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:" }).click();
  await page.getByRole("textbox", { name: "Password:" }).fill("12345678");
  await page.getByRole("button", { name: "Submit" }).click();

  await page.getByRole("textbox", { name: "Input your book:" }).click();
  await page
    .getByRole("textbox", { name: "Input your book:" })
    .fill("The Merry Adventures of Robin Hood by Howard Pyle");
  await page.getByRole("button", { name: "Submit" }).click();
  const books = page.getByTestId("generated-books").locator("p"); // Selects all paragraphs in 'generated-books' test-id
  await books.nth(0).getByRole("img").click(); // Clicks on the like button of the first book
  await books.nth(1).getByRole("img").click(); // Clicks on the like button of the second book
  const bookTitle1 = await books.nth(0).innerText(); // Get the book title of previously clicked first book
  const bookTitle2 = await books.nth(1).innerText(); // Get the book title of previously clicked second book
  await page.getByRole("link", { name: "Saved books" }).click();

  await expect(page).toHaveURL("http://localhost:5173/saved-books");
  await expect(page.getByText("Books you have saved")).toBeVisible();
  await expect(page.getByRole("heading")).toContainText("Books you have saved");
  await expect(page.getByText(bookTitle1)).toBeVisible();
  // await expect(page.getByRole("paragraph").nth(0)).toContainText(bookTitle1);
  await expect(page.getByText(bookTitle2)).toBeVisible();
  // await expect(page.getByRole("paragraph").nth(1)).toContainText(bookTitle2);
  await expect(page.getByRole("main")).toMatchAriaSnapshot(`
    - main:
      - heading "Books you have saved" [level=1]
      - paragraph
      - paragraph
    `);
});

test("should click like on three generated books, and see them displayed in 'saved books'", async ({
  page,
}) => {
  const email = `antanas+${uuidv4()}@inbox.lt`;

  await page.goto("http://localhost:5173/");
  await page.getByRole("link", { name: "Sign up" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:" }).click();
  await page.getByRole("textbox", { name: "Password:" }).fill("12345678");
  await page.getByRole("button", { name: "Submit" }).click();
  await page.locator(".alert.alert-success.alert-dismissible").waitFor(); // Waits for success sign up message

  await page.getByRole("link", { name: "Log in" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:" }).click();
  await page.getByRole("textbox", { name: "Password:" }).fill("12345678");
  await page.getByRole("button", { name: "Submit" }).click();

  await page.getByRole("textbox", { name: "Input your book:" }).click();
  await page
    .getByRole("textbox", { name: "Input your book:" })
    .fill("The Merry Adventures of Robin Hood by Howard Pyle");
  await page.getByRole("button", { name: "Submit" }).click();
  const books = page.getByTestId("generated-books").locator("p"); // Selects all paragraphs in 'generated-books' test-id
  await books.nth(0).getByRole("img").click(); // Clicks on the like button of the first book
  await books.nth(1).getByRole("img").click(); // Clicks on the like button of the second book
  await books.nth(2).getByRole("img").click(); // Clicks on the like button of the third book
  const bookTitle1 = await books.nth(0).innerText(); // Get the book title of previously clicked first book
  const bookTitle2 = await books.nth(1).innerText(); // Get the book title of previously clicked second book
  const bookTitle3 = await books.nth(2).innerText(); // Get the book title of previously clicked third book
  await page.getByRole("link", { name: "Saved books" }).click();

  await expect(page).toHaveURL("http://localhost:5173/saved-books");
  await expect(page.getByText("Books you have saved")).toBeVisible();
  await expect(page.getByRole("heading")).toContainText("Books you have saved");
  await expect(page.getByText(bookTitle1)).toBeVisible();
  await expect(page.getByText(bookTitle2)).toBeVisible();
  await expect(page.getByText(bookTitle3)).toBeVisible();
  await expect(page.getByRole("main")).toMatchAriaSnapshot(`
    - main:
      - heading "Books you have saved" [level=1]
      - paragraph
      - paragraph
      - paragraph
    `);
});
