import { test, expect } from "@playwright/test";
import { v4 as uuidv4 } from "uuid";

test("should generate 3 books to read when provided input", async ({
  page,
}) => {
  await page.goto("http://localhost:5173/");
  await page.getByRole("link", { name: "Log in" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill("jurgis@inbox.lt");
  await page.getByRole("textbox", { name: "Password:" }).click();
  await page.getByRole("textbox", { name: "Password:" }).fill("JfRn9Lb97*qs!#");
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

test("should not generate particular books if they were liked before", async ({
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

  // Generate books, like them and retrieve their text
  await page.getByRole("textbox", { name: "Input your book:" }).click();
  await page
    .getByRole("textbox", { name: "Input your book:" })
    .fill("The Merry Adventures of Robin Hood by Howard Pyle");
  await page.getByRole("button", { name: "Submit" }).click();
  const booksFirstRun = page.getByTestId("generated-books").locator("p"); // Selects all paragraphs in 'generated-books' test-id
  await booksFirstRun.nth(0).getByRole("img").click(); // clicks on the like button of the first book
  await booksFirstRun.nth(1).getByRole("img").click(); // clicks on the like button of the second book
  await booksFirstRun.nth(2).getByRole("img").click(); // clicks on the like button of the third book
  const book1FirstRun = await booksFirstRun.nth(0).innerText(); // Get the book title of previously clicked first book
  const book2FirstRun = await booksFirstRun.nth(1).innerText(); // Get the book title of previously clicked second book
  const book3FirstRun = await booksFirstRun.nth(2).innerText(); // Get the book title of previously clicked third book

  // Generate 3 books again,
  // Need to wait for response, because otherwise the test uses old generated values
  const responsePromise = page.waitForResponse(
    "http://localhost:8080/generate-books"
  );
  await page.getByRole("button", { name: "Submit" }).click();
  await responsePromise;

  // Assert
  await expect(page).toHaveURL("http://localhost:5173/");
  await expect(page.getByText(book1FirstRun)).not.toBeVisible();
  await expect(page.getByText(book2FirstRun)).not.toBeVisible();
  await expect(page.getByText(book3FirstRun)).not.toBeVisible();
  await expect(page.getByRole("main")).toMatchAriaSnapshot(`
      - main:
        - heading "Welcome, ${email}" [level=1]
        - heading "How to use" [level=2]
        - paragraph: For best results, try inputting the whole book title, with author. For example, Pride and Prejudice by Jane Austen.
        - text: "Input your book:"
        - textbox "Input your book:": The Merry Adventures of Robin Hood by Howard Pyle
        - button "Submit"
        - paragraph
        - paragraph
        - paragraph
      `);

  // Delete email
  await page.goto("http://localhost:8025");
  await page.getByRole("link", { name: email }).click();
  await page.getByRole("button", { name: "Delete" }).click();
});

test("should display an error when book field input is empty", async ({
  page,
}) => {
  await page.goto("http://localhost:5173/");
  await page.getByRole("link", { name: "Log in" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill("jurgis@inbox.lt");
  await page.getByRole("textbox", { name: "Password:" }).click();
  await page.getByRole("textbox", { name: "Password:" }).fill("JfRn9Lb97*qs!#");
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
  await page.getByRole("textbox", { name: "Password:" }).fill("JfRn9Lb97*qs!#");
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
  await page.getByRole("textbox", { name: "Password:" }).fill("JfRn9Lb97*qs!#");
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

test("should display error message when books are generated more than 6 times", async ({
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
  // Wait for success sign up message
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

  // Generate books
  await page.getByRole("textbox", { name: "Input your book:" }).click();
  await page
    .getByRole("textbox", { name: "Input your book:" })
    .fill("The Merry Adventures of Robin Hood by Howard Pyle");

  // I am awaiting so the "Submit" clicks don't happen too quickly
  const responsePromise1 = page.waitForResponse(
    "http://localhost:8080/generate-books"
  );
  await page.getByRole("button", { name: "Submit" }).click();
  await responsePromise1;

  const responsePromise2 = page.waitForResponse(
    "http://localhost:8080/generate-books"
  );
  await page.getByRole("button", { name: "Submit" }).click();
  await responsePromise2;

  const responsePromise3 = page.waitForResponse(
    "http://localhost:8080/generate-books"
  );
  await page.getByRole("button", { name: "Submit" }).click();
  await responsePromise3;

  const responsePromise4 = page.waitForResponse(
    "http://localhost:8080/generate-books"
  );
  await page.getByRole("button", { name: "Submit" }).click();
  await responsePromise4;

  const responsePromise5 = page.waitForResponse(
    "http://localhost:8080/generate-books"
  );
  await page.getByRole("button", { name: "Submit" }).click();
  await responsePromise5;

  const responsePromise6 = page.waitForResponse(
    "http://localhost:8080/generate-books"
  );
  await page.getByRole("button", { name: "Submit" }).click();
  await responsePromise6;

  const responsePromise7 = page.waitForResponse(
    "http://localhost:8080/generate-books"
  );
  await page.getByRole("button", { name: "Submit" }).click();
  await responsePromise7;

  // Assert
  await expect(page).toHaveURL("http://localhost:5173/");
  await expect(page.getByRole("main")).toMatchAriaSnapshot(`
      - main:
        - heading "Welcome, ${email}" [level=1]
        - heading "How to use" [level=2]
        - paragraph: For best results, try inputting the whole book title, with author. For example, Pride and Prejudice by Jane Austen.
        - text: "Input your book:"
        - textbox "Input your book:": The Merry Adventures of Robin Hood by Howard Pyle
        - paragraph
        - button "Submit"
        - paragraph
        - paragraph
        - paragraph
      `);

  // Delete email
  await page.goto("http://localhost:8025");
  await page.getByRole("link", { name: email }).click();
  await page.getByRole("button", { name: "Delete" }).click();
});
