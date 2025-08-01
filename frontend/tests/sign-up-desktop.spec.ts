import { test, expect } from "@playwright/test";
import { v4 as uuidv4 } from "uuid";

test("should sign up", async ({ page }) => {
  const email = `antanas+${uuidv4()}@inbox.lt`;

  await page.goto("http://localhost:5173/");
  await page.getByRole("link", { name: "Sign up" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:" }).click();
  await page.getByRole("textbox", { name: "Password:" }).fill("12345678");
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
  await page.getByRole("textbox", { name: "Password:" }).click();
  await page.getByRole("textbox", { name: "Password:" }).fill("12345678");
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
  await page.getByRole("textbox", { name: "Password:" }).click();
  await page.getByRole("textbox", { name: "Password:" }).fill("12345678");
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
  await page.getByRole("textbox", { name: "Password:" }).click();
  await page.getByRole("textbox", { name: "Password:" }).fill("12345678");
  await page.getByRole("button", { name: "Submit" }).click();

  await expect(page).toHaveURL("http://localhost:5173/signup");
  await expect(
    page.getByText("Email must be at most 255 characters long.")
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
  await page.getByRole("textbox", { name: "Password:" }).click();
  await page.getByRole("textbox", { name: "Password:" }).fill("12345");
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
  await page.getByRole("textbox", { name: "Password:" }).click();
  await page
    .getByRole("textbox", { name: "Password:" })
    .fill("123456789123456789123");
  await page.getByRole("button", { name: "Submit" }).click();

  await expect(page).toHaveURL("http://localhost:5173/signup");
  await expect(
    page.getByText("Password must be at most 20 characters long.")
  ).toBeVisible();
  await expect(page).toHaveScreenshot();
});
