import { test, expect, devices } from "@playwright/test";
import { v4 as uuidv4 } from "uuid";

test.use({
  ...devices["Pixel 5"],
});

test("should click like on a single generated book, and see the icon change color", async ({
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
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("textbox", { name: "Confirm password:" }).tap();
  await page
    .getByRole("textbox", { name: "Confirm password:" })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("button", { name: "Submit" }).tap();
  // Waits for success sign up message
  await page.locator(".alert.alert-success.alert-dismissible").waitFor();

  // Verify email
  await page.goto("http://localhost:8025");
  await page.getByRole("link", { name: email }).tap();
  const page1Promise = page.waitForEvent("popup");
  await page
    .locator("#preview-html")
    .contentFrame()
    .getByRole("link", { name: "http://localhost:8080/verify?" })
    .tap();
  const page1 = await page1Promise;

  // Log in
  await page.goto("http://localhost:5173");
  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Log in" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:" }).tap();
  await page.getByRole("textbox", { name: "Password:" }).fill("7VXuW8eJ#@F#iN");
  await page.getByRole("button", { name: "Submit" }).tap();

  // Generate books
  await page.getByRole("textbox", { name: "Input your book:" }).tap();
  await page
    .getByRole("textbox", { name: "Input your book:" })
    .fill("The Merry Adventures of Robin Hood by Howard Pyle");
  await page.getByRole("button", { name: "Submit" }).tap();
  const books = page.getByTestId("generated-books").locator("p"); // Selects all paragraphs in 'generated-books' test-id
  const firstBook = books.nth(0);
  const secondBook = books.nth(1);
  const thirdBook = books.nth(2);
  await firstBook.getByTestId(/^like-/).tap(); // taps on the like button of the first book

  // Assert
  await expect(page).toHaveURL("http://localhost:5173");
  await expect(firstBook.getByTestId(/^liked-/)).toBeVisible();
  await expect(secondBook.getByTestId(/^like-/)).toBeVisible();
  await expect(thirdBook.getByTestId(/^like-/)).toBeVisible();
  await expect(page.getByRole("main")).toMatchAriaSnapshot(`
    - main:
      - heading "Welcome, ${email}" [level=1]
      - heading "How to use" [level=2]
      - paragraph: "For best results, try inputting the whole book title, with author. For example, Pride and Prejudice by Jane Austen."
      - text: "Input your book:"
      - textbox "Input your book:": The Merry Adventures of Robin Hood by Howard Pyle
      - button "Submit"
      - paragraph
      - paragraph
      - paragraph
    `);
});
