import { test, expect, devices } from "@playwright/test";

test.use({
  ...devices["Pixel 5"],
});

test("should sign up", async ({ page }) => {
  const email = `antanas+${Date.now()}@inbox.lt`;

  await page.goto("http://localhost:5173/");
  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.getByRole("link", { name: "Sign up" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:" }).tap();
  await page.getByRole("textbox", { name: "Password:" }).fill("123456");
  await page.getByRole("button", { name: "Submit" }).click();
  await expect(page).toHaveURL("http://localhost:5173/");
  await expect(
    page.getByText("You have successfully signed up. You may now log in.")
  ).toBeVisible();
  await expect(page.getByRole("alert")).toContainText(
    "You have successfully signed up. You may now log in."
  );
});
