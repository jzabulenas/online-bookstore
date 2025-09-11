import test, { expect } from "@playwright/test";

test("should log out, when it is performed", async ({ page }) => {
  // Log in
  await page.goto("http://localhost:5173/");
  await page.getByRole("link", { name: "Log in" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill("jurgis@inbox.lt");
  await page.getByRole("textbox", { name: "Password:" }).click();
  await page.getByRole("textbox", { name: "Password:" }).fill("JfRn9Lb97*qs!#");
  await page.getByRole("button", { name: "Submit" }).click();
  await page
    .getByRole("heading", { name: "Welcome, jurgis@inbox.lt" })
    .waitFor();

  // Log out
  await page.getByRole("link", { name: "Log out" }).click();

  // Assert
  await expect(page).toHaveURL("http://localhost:5173/");
  await expect(
    page.getByRole("heading", { name: "Book recommendation app" })
  ).toBeVisible();
  await expect(page.locator("h1")).toContainText("Book recommendation app");
  await expect(page).toHaveScreenshot();
});
