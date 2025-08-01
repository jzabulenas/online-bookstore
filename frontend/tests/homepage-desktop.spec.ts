import { test, expect } from "@playwright/test";

test("should display homepage", async ({ page }) => {
  await page.goto("http://localhost:5173/");

  await expect(page).toHaveURL("http://localhost:5173/");
  await expect(
    page.getByRole("heading", { name: "Book recommendation app" })
  ).toBeVisible();
  await expect(page.locator("h1")).toContainText("Book recommendation app");
  await expect(page).toHaveScreenshot();
});
