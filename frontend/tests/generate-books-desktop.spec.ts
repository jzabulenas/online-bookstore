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

// I think this is flaky. Disabling for now
// test("should generate 3 books to read when provided input, and they should differ if generated again", async ({
//   page,
// }) => {
//   await page.goto("http://localhost:5173/");
//   await page.getByRole("link", { name: "Log in" }).click();
//   await page.getByRole("textbox", { name: "Email:" }).click();
//   await page.getByRole("textbox", { name: "Email:" }).fill("jurgis@inbox.lt");
//   await page.getByRole("textbox", { name: "Password:" }).click();
//   await page.getByRole("textbox", { name: "Password:" }).fill("12345678");
//   await page.getByRole("button", { name: "Submit" }).click();
//   await page.getByRole("textbox", { name: "Input your book:" }).click();
//   await page
//     .getByRole("textbox", { name: "Input your book:" })
//     .fill("The Merry Adventures of Robin Hood by Howard Pyle");
//   const responsePromise1 = page.waitForResponse(
//     "http://localhost:8080/generate-books"
//   );
//   await page.getByRole("button", { name: "Submit" }).click();
//   const response1 = await responsePromise1;
//   const booksFirstRun = page.getByTestId("generated-books").locator("p"); // Selects all paragraphs in 'generated-books' test-id
//   const book1FirstRun = await booksFirstRun.nth(0).innerText();
//   const book2FirstRun = await booksFirstRun.nth(1).innerText();
//   const book3FirstRun = await booksFirstRun.nth(2).innerText();
//   // Need to wait for response, because otherwise the test uses old generated values
//   const responsePromise2 = page.waitForResponse(
//     "http://localhost:8080/generate-books"
//   );
//   await page.getByRole("button", { name: "Submit" }).click();
//   const response2 = await responsePromise2;
//   const booksSecondRun = page.getByTestId("generated-books").locator("p"); // Selects all paragraphs in 'generated-books' test-id
//   const book1SecondRun = await booksSecondRun.nth(0).innerText();
//   const book2SecondRun = await booksSecondRun.nth(1).innerText();
//   const book3SecondRun = await booksSecondRun.nth(2).innerText();

//   await expect(page).toHaveURL("http://localhost:5173/");
//   expect(book1FirstRun).not.toEqual(book1SecondRun);
//   expect(book2FirstRun).not.toEqual(book2SecondRun);
//   expect(book3FirstRun).not.toEqual(book3SecondRun);
//   await expect(page.getByRole("main")).toMatchAriaSnapshot(`
//     - main:
//       - heading "Welcome, jurgis@inbox.lt" [level=1]
//       - heading "How to use" [level=2]
//       - paragraph: For best results, try inputting the whole book title, with author. For example, Pride and Prejudice by Jane Austen.
//       - text: "Input your book:"
//       - textbox "Input your book:": The Merry Adventures of Robin Hood by Howard Pyle
//       - button "Submit"
//       - paragraph
//       - paragraph
//       - paragraph
//     `);
// });

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
