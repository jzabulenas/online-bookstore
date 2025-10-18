import { test, expect } from "@playwright/test";
import { v4 as uuidv4 } from "uuid";

// The reason why I create a new user for each and every test, is because the
// individual tests themselves run at least three times, for each browser. I
// have to make sure there is no overlap

test("should click like on a single generated book, and see it displayed in 'saved books'", async ({
  page,
}) => {
  const email = `antanas+${uuidv4()}@inbox.lt`;

  // Sign up
  await page.goto("http://localhost:5173/");
  await page.getByRole("link", { name: "Sign up" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:", exact: true }).click();
  await page
    .getByRole("textbox", { name: "Password:", exact: true })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("textbox", { name: "Confirm password:" }).click();
  await page
    .getByRole("textbox", { name: "Confirm password:" })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("button", { name: "Submit" }).click();
  // Waits for success sign up message
  await page.locator(".alert.alert-success.alert-dismissible").waitFor();

  // Verify email
  await page.goto("http://localhost:8025");
  await page.getByRole("link", { name: email }).click();
  const page1Promise = page.waitForEvent("popup");
  await page
    .locator("#preview-html")
    .contentFrame()
    .getByRole("link", { name: "http://localhost:8080/verify?" })
    .click();
  const page1 = await page1Promise;

  // Log in
  await page.goto("http://localhost:5173");
  await page.getByRole("link", { name: "Log in" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:" }).click();
  await page.getByRole("textbox", { name: "Password:" }).fill("7VXuW8eJ#@F#iN");
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
  await expect(page.getByRole("main")).toMatchAriaSnapshot(`
    - main:
      - heading "Books you have saved" [level=1]
      - paragraph
    `);

  // Delete email
  await page.goto("http://localhost:8025");
  await page.getByRole("link", { name: email }).click();
  await page.getByRole("button", { name: "Delete" }).click();
});

test("should click like on two generated books, and see them displayed in 'saved books'", async ({
  page,
}) => {
  const email = `antanas+${uuidv4()}@inbox.lt`;

  // Sign up
  await page.goto("http://localhost:5173/");
  await page.getByRole("link", { name: "Sign up" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:", exact: true }).click();
  await page
    .getByRole("textbox", { name: "Password:", exact: true })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("textbox", { name: "Confirm password:" }).click();
  await page
    .getByRole("textbox", { name: "Confirm password:" })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("button", { name: "Submit" }).click();
  await page.locator(".alert.alert-success.alert-dismissible").waitFor(); // Waits for success sign up message

  // Verify email
  await page.goto("http://localhost:8025");
  await page.getByRole("link", { name: email }).click();
  const page1Promise = page.waitForEvent("popup");
  await page
    .locator("#preview-html")
    .contentFrame()
    .getByRole("link", { name: "http://localhost:8080/verify?" })
    .click();
  const page1 = await page1Promise;

  // Log in
  await page.goto("http://localhost:5173");
  await page.getByRole("link", { name: "Log in" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:" }).click();
  await page.getByRole("textbox", { name: "Password:" }).fill("7VXuW8eJ#@F#iN");
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
  await expect(page.getByText(bookTitle2)).toBeVisible();
  await expect(page.getByRole("main")).toMatchAriaSnapshot(`
    - main:
      - heading "Books you have saved" [level=1]
      - paragraph
      - paragraph
    `);

  // Delete email
  await page.goto("http://localhost:8025");
  await page.getByRole("link", { name: email }).click();
  await page.getByRole("button", { name: "Delete" }).click();
});

test("should click like on three generated books, and see them displayed in 'saved books'", async ({
  page,
}) => {
  const email = `antanas+${uuidv4()}@inbox.lt`;

  // Sign up
  await page.goto("http://localhost:5173/");
  await page.getByRole("link", { name: "Sign up" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:", exact: true }).click();
  await page
    .getByRole("textbox", { name: "Password:", exact: true })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("textbox", { name: "Confirm password:" }).click();
  await page
    .getByRole("textbox", { name: "Confirm password:" })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("button", { name: "Submit" }).click();
  await page.locator(".alert.alert-success.alert-dismissible").waitFor(); // Waits for success sign up message

  // Verify email
  await page.goto("http://localhost:8025");
  await page.getByRole("link", { name: email }).click();
  const page1Promise = page.waitForEvent("popup");
  await page
    .locator("#preview-html")
    .contentFrame()
    .getByRole("link", { name: "http://localhost:8080/verify?" })
    .click();
  const page1 = await page1Promise;

  // Log in
  await page.goto("http://localhost:5173");
  await page.getByRole("link", { name: "Log in" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:" }).click();
  await page.getByRole("textbox", { name: "Password:" }).fill("7VXuW8eJ#@F#iN");
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

  // Delete email
  await page.goto("http://localhost:8025");
  await page.getByRole("link", { name: email }).click();
  await page.getByRole("button", { name: "Delete" }).click();
});

test("should click like on generated books, and not see them displayed in 'saved books' for other user", async ({
  page,
}) => {
  const email = `antanas+${uuidv4()}@inbox.lt`;
  const email2 = `antanas+${uuidv4()}@inbox.lt`;

  // Sign up
  await page.goto("http://localhost:5173/");
  await page.getByRole("link", { name: "Sign up" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:", exact: true }).click();
  await page
    .getByRole("textbox", { name: "Password:", exact: true })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("textbox", { name: "Confirm password:" }).click();
  await page
    .getByRole("textbox", { name: "Confirm password:" })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("button", { name: "Submit" }).click();
  await page.locator(".alert.alert-success.alert-dismissible").waitFor(); // Waits for success sign up message

  // Verify email
  await page.goto("http://localhost:8025");
  await page.getByRole("link", { name: email }).click();
  const page1Promise = page.waitForEvent("popup");
  await page
    .locator("#preview-html")
    .contentFrame()
    .getByRole("link", { name: "http://localhost:8080/verify?" })
    .click();
  const page1 = await page1Promise;

  // Log in
  await page.goto("http://localhost:5173");
  await page.getByRole("link", { name: "Log in" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:" }).click();
  await page.getByRole("textbox", { name: "Password:" }).fill("7VXuW8eJ#@F#iN");
  await page.getByRole("button", { name: "Submit" }).click();

  // Generate books, click "like" on them, retrieve text
  await page.getByRole("textbox", { name: "Input your book:" }).click();
  await page
    .getByRole("textbox", { name: "Input your book:" })
    .fill("The Merry Adventures of Robin Hood by Howard Pyle");
  await page.getByRole("button", { name: "Submit" }).click();
  const books = page.getByTestId("generated-books").locator("p");
  await books.nth(0).getByRole("img").click();
  await books.nth(1).getByRole("img").click();
  await books.nth(2).getByRole("img").click();
  const bookTitle1 = await books.nth(0).innerText();
  const bookTitle2 = await books.nth(1).innerText();
  const bookTitle3 = await books.nth(2).innerText();

  // Log out
  await page.getByRole("link", { name: "Log out" }).click();

  // Sign up again
  await page.goto("http://localhost:5173/");
  await page.getByRole("link", { name: "Sign up" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill(email2);
  await page.getByRole("textbox", { name: "Password:", exact: true }).click();
  await page
    .getByRole("textbox", { name: "Password:", exact: true })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("textbox", { name: "Confirm password:" }).click();
  await page
    .getByRole("textbox", { name: "Confirm password:" })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("button", { name: "Submit" }).click();
  // Waits for success sign up message
  await page.locator(".alert.alert-success.alert-dismissible").waitFor();

  // Verify email
  await page.goto("http://localhost:8025");
  await page.getByRole("link", { name: email2 }).click();
  const page2Promise = page.waitForEvent("popup");
  await page
    .locator("#preview-html")
    .contentFrame()
    .getByRole("link", { name: "http://localhost:8080/verify?" })
    .click();
  const page2 = await page2Promise;

  // Log in
  await page.goto("http://localhost:5173");
  await page.getByRole("link", { name: "Log in" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill(email2);
  await page.getByRole("textbox", { name: "Password:" }).click();
  await page.getByRole("textbox", { name: "Password:" }).fill("7VXuW8eJ#@F#iN");
  await page.getByRole("button", { name: "Submit" }).click();

  // Go to "Saved books"
  // Waits for the text seen after log in
  await page.getByRole("textbox", { name: "Input your book:" }).waitFor();
  await page.getByRole("link", { name: "Saved books" }).click();

  // Assert
  await expect(page).toHaveURL("http://localhost:5173/saved-books");
  await expect(page.getByText("Books you have saved")).toBeVisible();
  await expect(page.getByRole("heading")).toContainText("Books you have saved");
  await expect(page.getByText(bookTitle1)).not.toBeVisible();
  await expect(page.getByText(bookTitle2)).not.toBeVisible();
  await expect(page.getByText(bookTitle3)).not.toBeVisible();
  await expect(page).toHaveScreenshot();

  // Delete email
  await page.goto("http://localhost:8025");
  await page.getByRole("link", { name: email }).click();
  await page.getByRole("button", { name: "Delete" }).click();

  // Delete email
  await page.goto("http://localhost:8025");
  await page.getByRole("link", { name: email2 }).click();
  await page.getByRole("button", { name: "Delete" }).click();
});

test("should not see liked books if no books are liked after generating books", async ({
  page,
}) => {
  const email = `antanas+${uuidv4()}@inbox.lt`;

  // Sign up
  await page.goto("http://localhost:5173/");
  await page.getByRole("link", { name: "Sign up" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:", exact: true }).click();
  await page
    .getByRole("textbox", { name: "Password:", exact: true })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("textbox", { name: "Confirm password:" }).click();
  await page
    .getByRole("textbox", { name: "Confirm password:" })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("button", { name: "Submit" }).click();
  await page.locator(".alert.alert-success.alert-dismissible").waitFor(); // Waits for success sign up message

  // Verify email
  await page.goto("http://localhost:8025");
  await page.getByRole("link", { name: email }).click();
  const page1Promise = page.waitForEvent("popup");
  await page
    .locator("#preview-html")
    .contentFrame()
    .getByRole("link", { name: "http://localhost:8080/verify?" })
    .click();
  const page1 = await page1Promise;

  // Log in
  await page.goto("http://localhost:5173");
  await page.getByRole("link", { name: "Log in" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:" }).click();
  await page.getByRole("textbox", { name: "Password:" }).fill("7VXuW8eJ#@F#iN");
  await page.getByRole("button", { name: "Submit" }).click();

  // Generate books, retrieve text
  await page.getByRole("textbox", { name: "Input your book:" }).click();
  await page
    .getByRole("textbox", { name: "Input your book:" })
    .fill("The Merry Adventures of Robin Hood by Howard Pyle");
  await page.getByRole("button", { name: "Submit" }).click();
  const books = page.getByTestId("generated-books").locator("p");
  const bookTitle1 = await books.nth(0).innerText();
  const bookTitle2 = await books.nth(1).innerText();
  const bookTitle3 = await books.nth(2).innerText();

  // Go to "Saved books"
  await page.getByRole("link", { name: "Saved books" }).click();

  // Assert
  await expect(page).toHaveURL("http://localhost:5173/saved-books");
  await expect(page.getByText("Books you have saved")).toBeVisible();
  await expect(page.getByRole("heading")).toContainText("Books you have saved");
  await expect(page.getByText(bookTitle1)).not.toBeVisible();
  await expect(page.getByText(bookTitle2)).not.toBeVisible();
  await expect(page.getByText(bookTitle3)).not.toBeVisible();
  await expect(page).toHaveScreenshot();

  // Delete email
  await page.goto("http://localhost:8025");
  await page.getByRole("link", { name: email }).click();
  await page.getByRole("button", { name: "Delete" }).click();
});
