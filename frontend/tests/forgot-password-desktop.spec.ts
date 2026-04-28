import { test, expect } from "@playwright/test";

test("should show confirmation message when valid email is submitted", async ({
  page,
}) => {
  await page.goto("http://localhost:5173/login");
  await page.getByRole("link", { name: "Forgot password?" }).click();

  await expect(page).toHaveURL("http://localhost:5173/forgot-password");
  await page.getByRole("textbox", { name: "Email:" }).fill("jurgis@inbox.lt");
  await page.getByRole("button", { name: "Send reset link" }).click();

  await expect(
    page.getByText(
      "If an account with that email exists, a password reset link has been sent. Please check your inbox.",
    ),
  ).toBeVisible();
  await expect(page).toHaveScreenshot();
});

test("should show confirmation message when unknown email is submitted", async ({
  page,
}) => {
  await page.goto("http://localhost:5173/forgot-password");
  await page
    .getByRole("textbox", { name: "Email:" })
    .fill("unknown@example.com");
  await page.getByRole("button", { name: "Send reset link" }).click();

  await expect(
    page.getByText(
      "If an account with that email exists, a password reset link has been sent. Please check your inbox.",
    ),
  ).toBeVisible();
  await expect(page).toHaveScreenshot();
});

test("should display error message when email is null", async ({ page }) => {
  await page.goto("http://localhost:5173/forgot-password");
  await page.getByRole("button", { name: "Send reset link" }).click();

  await expect(page.getByText("This field is required.")).toBeVisible();
  await expect(page).toHaveScreenshot();
});

test("should display error message when email is too short", async ({
  page,
}) => {
  await page.goto("http://localhost:5173/forgot-password");
  await page.getByRole("textbox", { name: "Email:" }).fill("f@b.c");
  await page.getByRole("button", { name: "Send reset link" }).click();

  await expect(
    page.getByText("Email must be at least 7 characters long."),
  ).toBeVisible();
  await expect(page).toHaveScreenshot();
});
