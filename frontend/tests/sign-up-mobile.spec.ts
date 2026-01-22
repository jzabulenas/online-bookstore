import { test, expect, devices } from "@playwright/test";
import { v4 as uuidv4 } from "uuid";

test.use({
  ...devices["Pixel 5"],
});

test("should sign up", async ({ page }) => {
  const email = `antanas+${uuidv4()}@inbox.lt`;

  // Fill form
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

  await expect(page).toHaveURL("http://localhost:5173/");
  await expect(
    page.getByText("Check your email to verify the account."),
  ).toBeVisible();
  await expect(page.getByRole("alert")).toContainText(
    "Check your email to verify the account.",
  );
  await expect(page).toHaveScreenshot();

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

  // Assert final page
  await expect(page1).toHaveURL("http://localhost:5173/verification-success");
  await expect(
    page1.getByText("Account activated! Feel free to log in."),
  ).toBeVisible();
  await expect(page1.getByRole("paragraph")).toContainText(
    "Account activated! Feel free to log in.",
  );
  await expect(page1).toHaveScreenshot();
});

test("should sign up with longest password possible", async ({ page }) => {
  const email = `antanas+${uuidv4()}@inbox.lt`;

  // Fill form
  await page.goto("http://localhost:5173/");
  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Sign up" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:", exact: true }).tap();
  await page
    .getByRole("textbox", { name: "Password:", exact: true })
    // This is 64 characters
    .fill("metyjwgaqakvjdrbpqsoywhrqzpesbrtsbtqfseffbivpfsaaihttjnjbmrbexbp");
  await page.getByRole("textbox", { name: "Confirm password:" }).tap();
  await page
    .getByRole("textbox", { name: "Confirm password:" })
    .fill("metyjwgaqakvjdrbpqsoywhrqzpesbrtsbtqfseffbivpfsaaihttjnjbmrbexbp");
  await page.getByRole("button", { name: "Submit" }).tap();

  await expect(page).toHaveURL("http://localhost:5173/");
  await expect(
    page.getByText("Check your email to verify the account."),
  ).toBeVisible();
  await expect(page.getByRole("alert")).toContainText(
    "Check your email to verify the account.",
  );
  await expect(page).toHaveScreenshot();

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

  // Assert final page
  await expect(page1).toHaveURL("http://localhost:5173/verification-success");
  await expect(
    page1.getByText("Account activated! Feel free to log in."),
  ).toBeVisible();
  await expect(page1.getByRole("paragraph")).toContainText(
    "Account activated! Feel free to log in.",
  );
  await expect(page1).toHaveScreenshot();
});

// Email
//
//
//
//
//
//
//
//
//

test("should display an error message when email is null", async ({ page }) => {
  await page.goto("http://localhost:5173/");
  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Sign up" }).tap();
  await page.getByRole("textbox", { name: "Password:", exact: true }).tap();
  await page
    .getByRole("textbox", { name: "Password:", exact: true })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("textbox", { name: "Confirm password:" }).tap();
  await page
    .getByRole("textbox", { name: "Confirm password:" })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("button", { name: "Submit" }).tap();

  await expect(page).toHaveURL("http://localhost:5173/signup");
  await expect(page.getByText("This field is required.")).toBeVisible();
  await expect(page).toHaveScreenshot();
});

test("should display an error message when email is too short", async ({
  page,
}) => {
  await page.goto("http://localhost:5173/");
  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Sign up" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).fill("f@b.c");
  await page.getByRole("textbox", { name: "Password:", exact: true }).tap();
  await page
    .getByRole("textbox", { name: "Password:", exact: true })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("textbox", { name: "Confirm password:" }).tap();
  await page
    .getByRole("textbox", { name: "Confirm password:" })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("button", { name: "Submit" }).tap();

  await expect(page).toHaveURL("http://localhost:5173/signup");
  await expect(
    page.getByText("Email must be at least 7 characters long."),
  ).toBeVisible();
  await expect(page).toHaveScreenshot();
});

test("should display an error message when email is too long", async ({
  page,
}) => {
  await page.goto("http://localhost:5173/");
  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Sign up" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).tap();
  await page
    .getByRole("textbox", { name: "Email:" })
    // This is 256 characters
    .fill(
      "dfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfs@gmail.com",
    );
  await page.getByRole("textbox", { name: "Password:", exact: true }).tap();
  await page
    .getByRole("textbox", { name: "Password:", exact: true })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("textbox", { name: "Confirm password:" }).tap();
  await page
    .getByRole("textbox", { name: "Confirm password:" })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("button", { name: "Submit" }).tap();

  await expect(page).toHaveURL("http://localhost:5173/signup");
  await expect(
    page.getByText("Email must be at most 255 characters long."),
  ).toBeVisible();
  await expect(page).toHaveScreenshot();
});

test("should display an error message when email already exists", async ({
  page,
}) => {
  await page.goto("http://localhost:5173/");
  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Sign up" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).tap();
  // This email already exists because it was created during global setup
  await page.getByRole("textbox", { name: "Email:" }).fill("jurgis@inbox.lt");
  await page.getByRole("textbox", { name: "Password:", exact: true }).tap();
  await page
    .getByRole("textbox", { name: "Password:", exact: true })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("textbox", { name: "Confirm password:" }).tap();
  await page
    .getByRole("textbox", { name: "Confirm password:" })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("button", { name: "Submit" }).tap();

  await expect(page).toHaveURL("http://localhost:5173/signup");
  await expect(
    page.getByText("Such email address is already in use"),
  ).toBeVisible();
  await expect(page).toHaveScreenshot();
});

// Password
//
//
//
//
//
//
//
//
//

test("should display an error message when password is null", async ({
  page,
}) => {
  const email = `antanas@inbox.lt`;

  await page.goto("http://localhost:5173/");
  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Sign up" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Confirm password:" }).tap();
  await page
    .getByRole("textbox", { name: "Confirm password:" })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("button", { name: "Submit" }).tap();

  await expect(page).toHaveURL("http://localhost:5173/signup");
  await expect(page.getByText("This field is required.")).toBeVisible();
  await expect(page.getByText("Passwords do not match.")).toBeVisible();
  await expect(page).toHaveScreenshot();
});

test("should display an error message when password is too short", async ({
  page,
}) => {
  const email = `antanas@inbox.lt`;

  await page.goto("http://localhost:5173/");
  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Sign up" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:", exact: true }).tap();
  await page
    .getByRole("textbox", { name: "Password:", exact: true })
    .fill("12345");
  await page.getByRole("textbox", { name: "Confirm password:" }).tap();
  await page.getByRole("textbox", { name: "Confirm password:" }).fill("12345");
  await page.getByRole("button", { name: "Submit" }).tap();

  await expect(page).toHaveURL("http://localhost:5173/signup");
  await expect(
    page.getByText("Password must be at least 14 characters long."),
  ).toBeVisible();
  await expect(page).toHaveScreenshot();
});

test("should display an error message when password is too long", async ({
  page,
}) => {
  const email = `antanas@inbox.lt`;

  await page.goto("http://localhost:5173/");
  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Sign up" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:", exact: true }).tap();
  await page
    .getByRole("textbox", { name: "Password:", exact: true })
    // 65 characters
    .fill("metyjwgaqakvjdrbpqsoywhrqzpesbrtsbtqfseffbivpfsaaihttjnjbmrbexbpe");
  await page.getByRole("textbox", { name: "Confirm password:" }).tap();
  await page
    .getByRole("textbox", { name: "Confirm password:" })
    .fill("metyjwgaqakvjdrbpqsoywhrqzpesbrtsbtqfseffbivpfsaaihttjnjbmrbexbpe");
  await page.getByRole("button", { name: "Submit" }).tap();

  await expect(page).toHaveURL("http://localhost:5173/signup");
  await expect(
    page.getByText("Password must be at most 64 characters long."),
  ).toBeVisible();
  await expect(page).toHaveScreenshot();
});

test("should display an error message when password is compromised", async ({
  page,
}) => {
  const email = `antanas@inbox.lt`;

  await page.goto("http://localhost:5173/");
  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Sign up" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:", exact: true }).tap();
  await page
    .getByRole("textbox", { name: "Password:", exact: true })
    .fill("12345678912345");
  await page.getByRole("textbox", { name: "Confirm password:" }).tap();
  await page
    .getByRole("textbox", { name: "Confirm password:" })
    .fill("12345678912345");
  await page.getByRole("button", { name: "Submit" }).tap();

  await expect(page).toHaveURL("http://localhost:5173/signup");
  await expect(
    page.getByText(
      "The provided password is compromised and cannot be used. Use something more unique",
    ),
  ).toBeVisible();
  await expect(page).toHaveScreenshot();
});

// test("should display an error message when password is of right length but does not contain uppercase letter", async ({
//   page,
// }) => {
//   const email = `antanas@inbox.lt`;

//   await page.goto("http://localhost:5173/");
//   await page.getByRole("button", { name: "Toggle navigation" }).tap();
//   await page.locator(".navbar-collapse.collapse.show").waitFor();
//   await page.getByRole("link", { name: "Sign up" }).tap();
//   await page.getByRole("textbox", { name: "Email:" }).tap();
//   await page.getByRole("textbox", { name: "Email:" }).fill(email);
//   await page.getByRole("textbox", { name: "Password:", exact: true }).tap();
//   await page
//     .getByRole("textbox", { name: "Password:", exact: true })
//     .fill("gd3k&$xvh!%q$t");
//   await page.getByRole("textbox", { name: "Confirm password:" }).tap();
//   await page
//     .getByRole("textbox", { name: "Confirm password:" })
//     .fill("gd3k&$xvh!%q$t");
//   await page.getByRole("button", { name: "Submit" }).tap();

//   await expect(page).toHaveURL("http://localhost:5173/signup");
//   await expect(
//     page.getByText(
//       "Password must contain at least one uppercase and lowercase letter, number and any of these symbols: !@#$%^&*"
//     )
//   ).toBeVisible();
//   await expect(page).toHaveScreenshot();
// });

// test("should display an error message when password is of right length but does not contain lowercase letter", async ({
//   page,
// }) => {
//   const email = `antanas@inbox.lt`;

//   await page.goto("http://localhost:5173/");
//   await page.getByRole("button", { name: "Toggle navigation" }).tap();
//   await page.locator(".navbar-collapse.collapse.show").waitFor();
//   await page.getByRole("link", { name: "Sign up" }).tap();
//   await page.getByRole("textbox", { name: "Email:" }).tap();
//   await page.getByRole("textbox", { name: "Email:" }).fill(email);
//   await page.getByRole("textbox", { name: "Password:", exact: true }).tap();
//   await page
//     .getByRole("textbox", { name: "Password:", exact: true })
//     .fill("S3FHCD4XMN!8T3");
//   await page.getByRole("textbox", { name: "Confirm password:" }).tap();
//   await page
//     .getByRole("textbox", { name: "Confirm password:" })
//     .fill("S3FHCD4XMN!8T3");
//   await page.getByRole("button", { name: "Submit" }).tap();

//   await expect(page).toHaveURL("http://localhost:5173/signup");
//   await expect(
//     page.getByText(
//       "Password must contain at least one uppercase and lowercase letter, number and any of these symbols: !@#$%^&*"
//     )
//   ).toBeVisible();
//   await expect(page).toHaveScreenshot();
// });

// test("should display an error message when password is of right length but does not contain number", async ({
//   page,
// }) => {
//   const email = `antanas@inbox.lt`;

//   await page.goto("http://localhost:5173/");
//   await page.getByRole("button", { name: "Toggle navigation" }).tap();
//   await page.locator(".navbar-collapse.collapse.show").waitFor();
//   await page.getByRole("link", { name: "Sign up" }).tap();
//   await page.getByRole("textbox", { name: "Email:" }).tap();
//   await page.getByRole("textbox", { name: "Email:" }).fill(email);
//   await page.getByRole("textbox", { name: "Password:", exact: true }).tap();
//   await page
//     .getByRole("textbox", { name: "Password:", exact: true })
//     .fill("vDGgVJbNHLjQAj");
//   await page.getByRole("textbox", { name: "Confirm password:" }).tap();
//   await page
//     .getByRole("textbox", { name: "Confirm password:" })
//     .fill("vDGgVJbNHLjQAj");
//   await page.getByRole("button", { name: "Submit" }).tap();

//   await expect(page).toHaveURL("http://localhost:5173/signup");
//   await expect(
//     page.getByText(
//       "Password must contain at least one uppercase and lowercase letter, number and any of these symbols: !@#$%^&*"
//     )
//   ).toBeVisible();
//   await expect(page).toHaveScreenshot();
// });

// test("should display an error message when password is of right length but does not contain special symbol", async ({
//   page,
// }) => {
//   const email = `antanas@inbox.lt`;

//   await page.goto("http://localhost:5173/");
//   await page.getByRole("button", { name: "Toggle navigation" }).tap();
//   await page.locator(".navbar-collapse.collapse.show").waitFor();
//   await page.getByRole("link", { name: "Sign up" }).tap();
//   await page.getByRole("textbox", { name: "Email:" }).tap();
//   await page.getByRole("textbox", { name: "Email:" }).fill(email);
//   await page.getByRole("textbox", { name: "Password:", exact: true }).tap();
//   await page
//     .getByRole("textbox", { name: "Password:", exact: true })
//     .fill("6paBNyxhAeLY65");
//   await page.getByRole("textbox", { name: "Confirm password:" }).tap();
//   await page
//     .getByRole("textbox", { name: "Confirm password:" })
//     .fill("6paBNyxhAeLY65");
//   await page.getByRole("button", { name: "Submit" }).tap();

//   await expect(page).toHaveURL("http://localhost:5173/signup");
//   await expect(
//     page.getByText(
//       "Password must contain at least one uppercase and lowercase letter, number and any of these symbols: !@#$%^&*"
//     )
//   ).toBeVisible();
//   await expect(page).toHaveScreenshot();
// });

// Confirm password
//
//
//
//
//
//
//
//
//

test("should display an error message when confirm password is null", async ({
  page,
}) => {
  const email = `antanas@inbox.lt`;

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
  await page.getByRole("button", { name: "Submit" }).tap();

  await expect(page).toHaveURL("http://localhost:5173/signup");
  await expect(page.getByText("This field is required.")).toBeVisible();
  await expect(page).toHaveScreenshot();
});

test("should display an error message when confirm password does not match password", async ({
  page,
}) => {
  const email = `antanas@inbox.lt`;

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
    .fill("1234567");
  await page.getByRole("button", { name: "Submit" }).tap();

  await expect(page).toHaveURL("http://localhost:5173/signup");
  await expect(page.getByText("Passwords do not match.")).toBeVisible();
  await expect(page).toHaveScreenshot();
});
