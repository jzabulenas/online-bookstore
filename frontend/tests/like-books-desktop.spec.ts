import { test, expect } from "@playwright/test";

test("should click like on a single generated book, and see it displayed in 'saved books'", async ({
  page,
}) => {
  const email = `antanas+${Date.now()}@inbox.lt`;

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
  await expect(page.getByRole("paragraph")).toContainText(bookTitle);
  await expect(page.getByRole("main")).toMatchAriaSnapshot(`
    - main:
      - heading "Books you have saved" [level=1]
      - paragraph
    `);
});
