import { test, expect } from "@playwright/test";
import { v4 as uuidv4 } from "uuid";

test("should sign up", async ({ page }) => {
  const email = `antanas+${uuidv4()}@inbox.lt`;

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

  await expect(page).toHaveURL("http://localhost:5173/");
  await expect(
    page.getByText("You have successfully signed up. You may now log in.")
  ).toBeVisible();
  await expect(page.getByRole("alert")).toContainText(
    "You have successfully signed up. You may now log in."
  );
  await expect(page).toHaveScreenshot();
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

test("should display an error message when email is empty", async ({
  page,
}) => {
  await page.goto("http://localhost:5173/");
  await page.getByRole("link", { name: "Sign up" }).click();
  await page.getByRole("textbox", { name: "Password:", exact: true }).click();
  await page
    .getByRole("textbox", { name: "Password:", exact: true })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("textbox", { name: "Confirm password:" }).click();
  await page
    .getByRole("textbox", { name: "Confirm password:" })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("button", { name: "Submit" }).click();

  await expect(page).toHaveURL("http://localhost:5173/signup");
  await expect(page.getByText("This field is required.")).toBeVisible();
  await expect(page).toHaveScreenshot();
});

test("should display an error message when email is too short", async ({
  page,
}) => {
  await page.goto("http://localhost:5173/");
  await page.getByRole("link", { name: "Sign up" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill("f@b.c");
  await page.getByRole("textbox", { name: "Password:", exact: true }).click();
  await page
    .getByRole("textbox", { name: "Password:", exact: true })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("textbox", { name: "Confirm password:" }).click();
  await page
    .getByRole("textbox", { name: "Confirm password:" })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("button", { name: "Submit" }).click();

  await expect(page).toHaveURL("http://localhost:5173/signup");
  await expect(
    page.getByText("Email must be at least 7 characters long.")
  ).toBeVisible();
  await expect(page).toHaveScreenshot();
});

test("should display an error message when email is too long", async ({
  page,
}) => {
  await page.goto("http://localhost:5173/");
  await page.getByRole("link", { name: "Sign up" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page
    .getByRole("textbox", { name: "Email:" })
    // This is 256 characters
    .fill(
      "dfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfsferqerdfs@gmail.com"
    );
  await page.getByRole("textbox", { name: "Password:", exact: true }).click();
  await page
    .getByRole("textbox", { name: "Password:", exact: true })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("textbox", { name: "Confirm password:" }).click();
  await page
    .getByRole("textbox", { name: "Confirm password:" })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("button", { name: "Submit" }).click();

  await expect(page).toHaveURL("http://localhost:5173/signup");
  await expect(
    page.getByText("Email must be at most 255 characters long.")
  ).toBeVisible();
  await expect(page).toHaveScreenshot();
});

test("should display an error message when email already exists", async ({
  page,
}) => {
  await page.goto("http://localhost:5173/");
  await page.getByRole("link", { name: "Sign up" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  // This email already exists because it was created during global setup
  await page.getByRole("textbox", { name: "Email:" }).fill("jurgis@inbox.lt");
  await page.getByRole("textbox", { name: "Password:", exact: true }).click();
  await page
    .getByRole("textbox", { name: "Password:", exact: true })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("textbox", { name: "Confirm password:" }).click();
  await page
    .getByRole("textbox", { name: "Confirm password:" })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("button", { name: "Submit" }).click();

  await expect(page).toHaveURL("http://localhost:5173/signup");
  await expect(
    page.getByText("Such email address is already in use.")
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

test("should display an error message when password is empty", async ({
  page,
}) => {
  const email = `antanas@inbox.lt`;

  await page.goto("http://localhost:5173/");
  await page.getByRole("link", { name: "Sign up" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Confirm password:" }).click();
  await page
    .getByRole("textbox", { name: "Confirm password:" })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("button", { name: "Submit" }).click();

  await expect(page).toHaveURL("http://localhost:5173/signup");
  await expect(page.getByText("This field is required.")).toBeVisible();
  await expect(page).toHaveScreenshot();
});

test("should display an error message when password is too short", async ({
  page,
}) => {
  const email = `antanas@inbox.lt`;

  await page.goto("http://localhost:5173/");
  await page.getByRole("link", { name: "Sign up" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:", exact: true }).click();
  await page
    .getByRole("textbox", { name: "Password:", exact: true })
    .fill("12345");
  await page.getByRole("textbox", { name: "Confirm password:" }).click();
  await page.getByRole("textbox", { name: "Confirm password:" }).fill("12345");
  await page.getByRole("button", { name: "Submit" }).click();

  await expect(page).toHaveURL("http://localhost:5173/signup");
  await expect(
    page.getByText("Password must be at least 8 characters long.")
  ).toBeVisible();
  await expect(page).toHaveScreenshot();
});

test("should display an error message when password is too long", async ({
  page,
}) => {
  const email = `antanas@inbox.lt`;

  await page.goto("http://localhost:5173/");
  await page.getByRole("link", { name: "Sign up" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:", exact: true }).click();
  await page
    .getByRole("textbox", { name: "Password:", exact: true })
    .fill("7VXuW8eJ#@F#iN97VXuW8eJ#@F#iN9123");
  await page.getByRole("textbox", { name: "Confirm password:" }).click();
  await page
    .getByRole("textbox", { name: "Confirm password:" })
    .fill("7VXuW8eJ#@F#iN97VXuW8eJ#@F#iN9123");
  await page.getByRole("button", { name: "Submit" }).click();

  await expect(page).toHaveURL("http://localhost:5173/signup");
  await expect(
    page.getByText("Password must be at most 20 characters long.")
  ).toBeVisible();
  await expect(page).toHaveScreenshot();
});

test("should display an error message when password is of right length but does not contain uppercase letter", async ({
  page,
}) => {
  const email = `antanas@inbox.lt`;

  await page.goto("http://localhost:5173/");
  await page.getByRole("link", { name: "Sign up" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:", exact: true }).click();
  await page
    .getByRole("textbox", { name: "Password:", exact: true })
    .fill("gd3k&$xvh!%q$t");
  await page.getByRole("textbox", { name: "Confirm password:" }).click();
  await page
    .getByRole("textbox", { name: "Confirm password:" })
    .fill("gd3k&$xvh!%q$t");
  await page.getByRole("button", { name: "Submit" }).click();

  await expect(page).toHaveURL("http://localhost:5173/signup");
  await expect(
    page.getByText(
      "Password must contain at least one uppercase and lowercase letter, number and any of these symbols: !@#$%^&*"
    )
  ).toBeVisible();
  await expect(page).toHaveScreenshot();
});

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

test("should display an error message when confirm password is empty", async ({
  page,
}) => {
  const email = `antanas@inbox.lt`;

  await page.goto("http://localhost:5173/");
  await page.getByRole("link", { name: "Sign up" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:", exact: true }).click();
  await page
    .getByRole("textbox", { name: "Password:", exact: true })
    .fill("7VXuW8eJ#@F#iN");
  await page.getByRole("button", { name: "Submit" }).click();

  await expect(page).toHaveURL("http://localhost:5173/signup");
  await expect(page.getByText("This field is required.")).toBeVisible();
  await expect(page).toHaveScreenshot();
});

test("should display an error message when confirm password does not match password", async ({
  page,
}) => {
  const email = `antanas@inbox.lt`;

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
    .fill("1234567");
  await page.getByRole("button", { name: "Submit" }).click();

  await expect(page).toHaveURL("http://localhost:5173/signup");
  await expect(page.getByText("Passwords do not match.")).toBeVisible();
  await expect(page).toHaveScreenshot();
});
