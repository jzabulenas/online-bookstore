import { test, expect, devices } from "@playwright/test";

test.use({
  ...devices["Pixel 5"],
});

test("should log out, when it is performed", async ({ page }) => {
  // Log in
  await page.goto("http://localhost:5173/");
  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Log in" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).fill("jurgis@inbox.lt");
  await page.getByRole("textbox", { name: "Password:" }).tap();
  await page.getByRole("textbox", { name: "Password:" }).fill("JfRn9Lb97*qs!#");
  await page.getByRole("button", { name: "Submit" }).tap();
  await page
    .getByRole("heading", { name: "Welcome, jurgis@inbox.lt" })
    .waitFor();

  // Log out
  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Log out" }).tap();

  // Assert
  await expect(page).toHaveURL("http://localhost:5173/");
  await expect(
    page.getByRole("heading", { name: "Book recommendation app" })
  ).toBeVisible();
  await expect(page.locator("h1")).toContainText("Book recommendation app");
  await expect(page).toHaveScreenshot();
});
