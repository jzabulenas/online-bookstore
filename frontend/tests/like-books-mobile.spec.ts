import { test, expect, devices } from "@playwright/test";
import { v4 as uuidv4 } from "uuid";

test.use({
  ...devices["Pixel 5"],
});

// The reason why I create a new user for each and every test, is because the
// individual tests themselves run at least three times, for each browser. I
// have to make sure there is no overlap

test("should click like on a single generated book, and see it displayed in 'saved books'", async ({
  page,
}) => {
  const email = `antanas+${uuidv4()}@inbox.lt`;

  await page.goto("http://localhost:5173/");
  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Sign up" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:", exact: true }).tap();
  await page
    .getByRole("textbox", { name: "Password:", exact: true })
    .fill("12345678");
  await page.getByRole("textbox", { name: "Confirm password:" }).tap();
  await page
    .getByRole("textbox", { name: "Confirm password:" })
    .fill("12345678");

  await page.getByRole("button", { name: "Submit" }).tap();
  await page.locator(".alert.alert-success.alert-dismissible").waitFor(); // Waits for success sign up message

  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Log in" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:" }).tap();
  await page.getByRole("textbox", { name: "Password:" }).fill("12345678");
  await page.getByRole("button", { name: "Submit" }).tap();

  await page.getByRole("textbox", { name: "Input your book:" }).tap();
  await page
    .getByRole("textbox", { name: "Input your book:" })
    .fill("The Merry Adventures of Robin Hood by Howard Pyle");
  await page.getByRole("button", { name: "Submit" }).tap();
  const books = page.getByTestId("generated-books").locator("p"); // Selects all paragraphs in 'generated-books' test-id
  await books.nth(0).getByRole("img").tap(); // taps on the like button of the first paragraph
  const bookTitle = await books.nth(0).innerText(); // Get the book title of previously taped book
  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Saved books" }).tap();

  await expect(page).toHaveURL("http://localhost:5173/saved-books");
  await expect(page.getByText("Books you have saved")).toBeVisible();
  await expect(page.getByRole("heading")).toContainText("Books you have saved");
  await expect(page.getByText(bookTitle)).toBeVisible();
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
  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Sign up" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:", exact: true }).tap();
  await page
    .getByRole("textbox", { name: "Password:", exact: true })
    .fill("12345678");
  await page.getByRole("textbox", { name: "Confirm password:" }).tap();
  await page
    .getByRole("textbox", { name: "Confirm password:" })
    .fill("12345678");
  await page.getByRole("button", { name: "Submit" }).tap();
  await page.locator(".alert.alert-success.alert-dismissible").waitFor(); // Waits for success sign up message

  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Log in" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:" }).tap();
  await page.getByRole("textbox", { name: "Password:" }).fill("12345678");
  await page.getByRole("button", { name: "Submit" }).tap();

  await page.getByRole("textbox", { name: "Input your book:" }).tap();
  await page
    .getByRole("textbox", { name: "Input your book:" })
    .fill("The Merry Adventures of Robin Hood by Howard Pyle");
  await page.getByRole("button", { name: "Submit" }).tap();
  const books = page.getByTestId("generated-books").locator("p"); // Selects all paragraphs in 'generated-books' test-id
  await books.nth(0).getByRole("img").tap(); // taps on the like button of the first book
  await books.nth(1).getByRole("img").tap(); // taps on the like button of the second book
  const bookTitle1 = await books.nth(0).innerText(); // Get the book title of previously taped first book
  const bookTitle2 = await books.nth(1).innerText(); // Get the book title of previously taped second book
  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Saved books" }).tap();

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
});

test("should click like on three generated books, and see them displayed in 'saved books'", async ({
  page,
}) => {
  const email = `antanas+${uuidv4()}@inbox.lt`;

  await page.goto("http://localhost:5173/");
  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Sign up" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:", exact: true }).tap();
  await page
    .getByRole("textbox", { name: "Password:", exact: true })
    .fill("12345678");
  await page.getByRole("textbox", { name: "Confirm password:" }).tap();
  await page
    .getByRole("textbox", { name: "Confirm password:" })
    .fill("12345678");
  await page.getByRole("button", { name: "Submit" }).tap();
  await page.locator(".alert.alert-success.alert-dismissible").waitFor(); // Waits for success sign up message

  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Log in" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:" }).tap();
  await page.getByRole("textbox", { name: "Password:" }).fill("12345678");
  await page.getByRole("button", { name: "Submit" }).tap();

  await page.getByRole("textbox", { name: "Input your book:" }).tap();
  await page
    .getByRole("textbox", { name: "Input your book:" })
    .fill("The Merry Adventures of Robin Hood by Howard Pyle");
  await page.getByRole("button", { name: "Submit" }).tap();
  const books = page.getByTestId("generated-books").locator("p"); // Selects all paragraphs in 'generated-books' test-id
  await books.nth(0).getByRole("img").tap(); // taps on the like button of the first book
  await books.nth(1).getByRole("img").tap(); // taps on the like button of the second book
  await books.nth(2).getByRole("img").tap(); // taps on the like button of the third book
  const bookTitle1 = await books.nth(0).innerText(); // Get the book title of previously taped first book
  const bookTitle2 = await books.nth(1).innerText(); // Get the book title of previously taped second book
  const bookTitle3 = await books.nth(2).innerText(); // Get the book title of previously taped third book
  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Saved books" }).tap();

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

test("should click like on generated books, and not see them displayed in 'saved books' for other user", async ({
  page,
}) => {
  const email = `antanas+${uuidv4()}@inbox.lt`;
  const email2 = `antanas+${uuidv4()}@inbox.lt`;

  // Sign up
  await page.goto("http://localhost:5173/");
  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Sign up" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:", exact: true }).tap();
  await page
    .getByRole("textbox", { name: "Password:", exact: true })
    .fill("12345678");
  await page.getByRole("textbox", { name: "Confirm password:" }).tap();
  await page
    .getByRole("textbox", { name: "Confirm password:" })
    .fill("12345678");
  await page.getByRole("button", { name: "Submit" }).tap();
  await page.locator(".alert.alert-success.alert-dismissible").waitFor(); // Waits for success sign up message

  // Log in
  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Log in" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:" }).tap();
  await page.getByRole("textbox", { name: "Password:" }).fill("12345678");
  await page.getByRole("button", { name: "Submit" }).tap();

  // Generate books, click "like" on them, retrieve text
  await page.getByRole("textbox", { name: "Input your book:" }).tap();
  await page
    .getByRole("textbox", { name: "Input your book:" })
    .fill("The Merry Adventures of Robin Hood by Howard Pyle");
  await page.getByRole("button", { name: "Submit" }).tap();
  const books = page.getByTestId("generated-books").locator("p");
  await books.nth(0).getByRole("img").tap();
  await books.nth(1).getByRole("img").tap();
  await books.nth(2).getByRole("img").tap();
  const bookTitle1 = await books.nth(0).innerText();
  const bookTitle2 = await books.nth(1).innerText();
  const bookTitle3 = await books.nth(2).innerText();

  // Log out
  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Log out" }).tap();

  // Sign up again
  await page.goto("http://localhost:5173/");
  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Sign up" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).fill(email2);
  await page.getByRole("textbox", { name: "Password:", exact: true }).tap();
  await page
    .getByRole("textbox", { name: "Password:", exact: true })
    .fill("12345678");
  await page.getByRole("textbox", { name: "Confirm password:" }).tap();
  await page
    .getByRole("textbox", { name: "Confirm password:" })
    .fill("12345678");
  await page.getByRole("button", { name: "Submit" }).tap();
  await page.locator(".alert.alert-success.alert-dismissible").waitFor(); // Waits for success sign up message

  // Log in
  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Log in" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).fill(email2);
  await page.getByRole("textbox", { name: "Password:" }).tap();
  await page.getByRole("textbox", { name: "Password:" }).fill("12345678");
  await page.getByRole("button", { name: "Submit" }).tap();

  // Go to "Saved books"
  await page.getByRole("textbox", { name: "Input your book:" }).waitFor(); // Waits for the text seen after log in
  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Saved books" }).tap();

  // Assert
  await expect(page).toHaveURL("http://localhost:5173/saved-books");
  await expect(page.getByText("Books you have saved")).toBeVisible();
  await expect(page.getByRole("heading")).toContainText("Books you have saved");
  await expect(page.getByText(bookTitle1)).not.toBeVisible();
  await expect(page.getByText(bookTitle2)).not.toBeVisible();
  await expect(page.getByText(bookTitle3)).not.toBeVisible();
  await expect(page).toHaveScreenshot();
});

test("should not see liked books if no books are liked after generating books", async ({
  page,
}) => {
  const email = `antanas+${uuidv4()}@inbox.lt`;

  // Sign up
  await page.goto("http://localhost:5173/");
  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Sign up" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:", exact: true }).tap();
  await page
    .getByRole("textbox", { name: "Password:", exact: true })
    .fill("12345678");
  await page.getByRole("textbox", { name: "Confirm password:" }).tap();
  await page
    .getByRole("textbox", { name: "Confirm password:" })
    .fill("12345678");
  await page.getByRole("button", { name: "Submit" }).tap();
  await page.locator(".alert.alert-success.alert-dismissible").waitFor(); // Waits for success sign up message

  // Log in
  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Log in" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:" }).tap();
  await page.getByRole("textbox", { name: "Password:" }).fill("12345678");
  await page.getByRole("button", { name: "Submit" }).tap();

  // Generate books, retrieve text
  await page.getByRole("textbox", { name: "Input your book:" }).tap();
  await page
    .getByRole("textbox", { name: "Input your book:" })
    .fill("The Merry Adventures of Robin Hood by Howard Pyle");
  await page.getByRole("button", { name: "Submit" }).tap();
  const books = page.getByTestId("generated-books").locator("p");
  const bookTitle1 = await books.nth(0).innerText();
  const bookTitle2 = await books.nth(1).innerText();
  const bookTitle3 = await books.nth(2).innerText();

  // Go to "Saved books"
  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Saved books" }).tap();

  // Assert
  await expect(page).toHaveURL("http://localhost:5173/saved-books");
  await expect(page.getByText("Books you have saved")).toBeVisible();
  await expect(page.getByRole("heading")).toContainText("Books you have saved");
  await expect(page.getByText(bookTitle1)).not.toBeVisible();
  await expect(page.getByText(bookTitle2)).not.toBeVisible();
  await expect(page.getByText(bookTitle3)).not.toBeVisible();
  await expect(page).toHaveScreenshot();
});
