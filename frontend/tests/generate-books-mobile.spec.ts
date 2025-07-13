import { test, expect, devices } from "@playwright/test";

test.use({
  ...devices["Pixel 5"],
});

test("should generate 3 books to read when provided input", async ({
  page,
}) => {
  await page.goto("http://localhost:5173/");
  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Log in" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).fill("jurgis@inbox.lt");
  await page.getByRole("textbox", { name: "Password:" }).tap();
  await page.getByRole("textbox", { name: "Password:" }).fill("12345678");
  await page.getByRole("button", { name: "Submit" }).tap();
  await page.getByRole("textbox", { name: "Input your book:" }).tap();
  await page
    .getByRole("textbox", { name: "Input your book:" })
    .fill("The Merry Adventures of Robin Hood by Howard Pyle");
  await page.getByRole("button", { name: "Submit" }).tap();

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
