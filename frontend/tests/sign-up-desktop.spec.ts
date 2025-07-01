import { test, expect } from "@playwright/test";

test("should sign up", async ({ page }) => {
  const email = `antanas+${Date.now()}@inbox.lt`;

  await page.goto("http://localhost:5173/");
  await page.getByRole("link", { name: "Sign up" }).click();
  await page.getByRole("textbox", { name: "Email:" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("textbox", { name: "Password:" }).click();
  await page.getByRole("textbox", { name: "Password:" }).fill("123456");
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

test("should display an error message when email is empty", async ({
  page,
}) => {
  await page.goto("http://localhost:5173/");
  await page.getByRole("link", { name: "Sign up" }).click();
  await page.getByRole("textbox", { name: "Password:" }).click();
  await page.getByRole("textbox", { name: "Password:" }).fill("123456");
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
  await page.getByRole("textbox", { name: "Password:" }).fill("123456");
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
  await page.getByRole("textbox", { name: "Password:" }).fill("123456");
  await page.getByRole("button", { name: "Submit" }).click();

  await expect(page).toHaveURL("http://localhost:5173/signup");
  await expect(
    page.getByText("Email must be at most 255 characters long.")
  ).toBeVisible();
  await expect(page).toHaveScreenshot();
});
