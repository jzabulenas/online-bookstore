import { test, expect } from "@playwright/test";

test("should generate 3 books to read when provided input", async ({
  page,
}) => {
  await page.goto("http://localhost:5173/");
  await page.getByRole("link", { name: "Log in" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill("jurgis@inbox.lt");
  await page.getByRole("textbox", { name: "Password:" }).click();
  await page.getByRole("textbox", { name: "Password:" }).fill("12345678");
  await page.getByRole("button", { name: "Submit" }).click();
  await page.getByRole("textbox", { name: "Input your book:" }).click();
  await page
    .getByRole("textbox", { name: "Input your book:" })
    .fill("The Merry Adventures of Robin Hood by Howard Pyle");
  await page.getByRole("button", { name: "Submit" }).click();

  await expect(page).toHaveURL("http://localhost:5173/");
  await expect(page.getByRole("main")).toMatchAriaSnapshot(`
    - main:
      - heading "Welcome, jurgis@inbox.lt" [level=1]
      - heading "How to use" [level=2]
      - paragraph: For best results, try inputting the whole book title, with author. For example, Pride and Prejudice by Jane Austen.
      - text: "Input your book:"
      - textbox "Input your book:": The Merry Adventures of Robin Hood by Howard Pyle
      - button "Submit"
      - paragraph
      - paragraph
      - paragraph
    `);
});

test("should display an error when book field input is empty", async ({
  page,
}) => {
  await page.goto("http://localhost:5173/");
  await page.getByRole("link", { name: "Log in" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill("jurgis@inbox.lt");
  await page.getByRole("textbox", { name: "Password:" }).click();
  await page.getByRole("textbox", { name: "Password:" }).fill("12345678");
  await page.getByRole("button", { name: "Submit" }).click();
  await page.getByRole("textbox", { name: "Input your book:" }).click();
  await page.getByRole("button", { name: "Submit" }).click();

  await expect(page).toHaveURL("http://localhost:5173/");
  await expect(page.getByText("Cannot be empty.")).toBeVisible();
  await expect(page).toHaveScreenshot();
});

test("should display an error when book field input is too short", async ({
  page,
}) => {
  await page.goto("http://localhost:5173/");
  await page.getByRole("link", { name: "Log in" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill("jurgis@inbox.lt");
  await page.getByRole("textbox", { name: "Password:" }).click();
  await page.getByRole("textbox", { name: "Password:" }).fill("12345678");
  await page.getByRole("button", { name: "Submit" }).click();
  await page.getByRole("textbox", { name: "Input your book:" }).click();
  await page.getByRole("textbox", { name: "Input your book:" }).fill("The ");
  await page.getByRole("button", { name: "Submit" }).click();

  await expect(page).toHaveURL("http://localhost:5173/");
  await expect(
    page.getByText("Must be at least 5 characters long.")
  ).toBeVisible();
  await expect(page).toHaveScreenshot();
});

test("should display an error when book field input is too long", async ({
  page,
}) => {
  await page.goto("http://localhost:5173/");
  await page.getByRole("link", { name: "Log in" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill("jurgis@inbox.lt");
  await page.getByRole("textbox", { name: "Password:" }).click();
  await page.getByRole("textbox", { name: "Password:" }).fill("12345678");
  await page.getByRole("button", { name: "Submit" }).click();
  await page.getByRole("textbox", { name: "Input your book:" }).click();
  await page
    .getByRole("textbox", { name: "Input your book:" })
    .fill(
      "jurgisjurgisjurgisjurgisjurgisjurgisjurgisjurgisjurgisjurgisjurgisjurgisjurgisjurgisjurgisjurgisjurgis"
    ); // 102 characters
  await page.getByRole("button", { name: "Submit" }).click();

  await expect(page).toHaveURL("http://localhost:5173/");
  await expect(
    page.getByText("Must be at most 100 characters long.")
  ).toBeVisible();
  await expect(page).toHaveScreenshot();
});
