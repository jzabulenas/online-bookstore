import { test, expect, devices } from "@playwright/test";

test.use({
  ...devices["Pixel 5"],
});

test("should log in, when correct credentials are provided", async ({
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

  await expect(page).toHaveURL("http://localhost:5173/");
  await expect(
    page.getByRole("heading", { name: "Welcome, jurgis@inbox.lt" })
  ).toBeVisible();
  await expect(page.locator("h1")).toContainText("Welcome, jurgis@inbox.lt");
  await expect(page).toHaveScreenshot();
});

test("should display error message, when log in credentials are incorrect", async ({
  page,
}) => {
  await page.goto("http://localhost:5173/");
  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Log in" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).fill("jurgis@inbox.lt");
  await page.getByRole("textbox", { name: "Email:" }).tap();
  await page.getByRole("textbox", { name: "Password:" }).fill("12345");
  await page.getByRole("button", { name: "Submit" }).tap();

  await expect(page).toHaveURL("http://localhost:5173/login");
  await expect(page.getByText("Username or password is")).toBeVisible();
  await expect(page.getByRole("paragraph")).toContainText(
    "Username or password is incorrect."
  );
  await expect(page).toHaveScreenshot();
});
