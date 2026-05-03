import { test, expect } from "@playwright/test";
import { v4 as uuidv4 } from "uuid";

test("should reset password successfully", async ({ page }) => {
  const email = `antanas+${uuidv4()}@inbox.lt`;
  const oldPassword = "r9$CbHEaGXLUsP";
  const newPassword = "r9$CbHEaGXLUsQ";

  // Sign up
  await page.goto("http://localhost:5173/");
  await page.getByRole("link", { name: "Sign up" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page
    .getByRole("textbox", { name: "Password:", exact: true })
    .fill(oldPassword);
  await page
    .getByRole("textbox", { name: "Confirm password:" })
    .fill(oldPassword);
  await page.getByRole("button", { name: "Submit" }).click();
  await expect(
    page.getByText("Check your email to verify the account."),
  ).toBeVisible();

  // Verify account via email
  await page.goto("http://localhost:8025");
  await page.getByRole("link", { name: email }).click();
  const verifyPopupPromise = page.waitForEvent("popup");
  await page
    .locator("#preview-html")
    .contentFrame()
    .getByRole("link", { name: "http://localhost:8080/verify?" })
    .click();
  const verifyPopup = await verifyPopupPromise;
  await verifyPopup.waitForURL("**/verification-success");

  // Request password reset
  await page.goto("http://localhost:5173/login");
  await page.getByRole("link", { name: "Forgot password?" }).click();
  await expect(page.getByText("Send reset link")).toBeVisible();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("button", { name: "Send reset link" }).click();
  await expect(
    page.getByText("If an account with that email exists"),
  ).toBeVisible();

  // Open reset link from email
  await page.goto("http://localhost:8025");
  await page
    .getByRole("link", { name: email })
    .filter({ hasText: "Password Reset Request" })
    .click();
  const resetPopupPromise = page.waitForEvent("popup");
  await page
    .locator("#preview-html")
    .contentFrame()
    .getByRole("link", { name: "http://localhost:5173/reset-password?" })
    .click();
  const resetPage = await resetPopupPromise;
  await resetPage.waitForURL("**/reset-password**");

  // Reset the password
  await resetPage
    .getByRole("textbox", { name: "New password:", exact: true })
    .fill(newPassword);
  await resetPage
    .getByRole("textbox", { name: "Confirm new password:" })
    .fill(newPassword);
  await resetPage.getByRole("button", { name: "Reset password" }).click();

  await expect(resetPage).toHaveURL(
    "http://localhost:5173/reset-password-success",
  );
  await expect(
    resetPage.getByText("Your password has been reset successfully."),
  ).toBeVisible();
  await expect(resetPage).toHaveScreenshot();

  // Verify the new password works
  await resetPage.goto("http://localhost:5173/login");
  await resetPage.getByRole("textbox", { name: "Email:" }).fill(email);
  await resetPage.getByRole("textbox", { name: "Password:" }).fill(newPassword);
  await resetPage.getByRole("button", { name: "Submit" }).click();

  await expect(resetPage).toHaveURL("http://localhost:5173/");
  await expect(
    resetPage.getByRole("heading", { name: `Welcome, ${email}` }),
  ).toBeVisible();
});

test("should display error when password reset link has no token", async ({
  page,
}) => {
  await page.goto("http://localhost:5173/reset-password");

  await expect(page.getByText("Invalid password reset link.")).toBeVisible();
  await expect(page).toHaveScreenshot();
});

test("should display error when token is invalid", async ({ page }) => {
  // Needed to get CSRF token
  await page.goto("http://localhost:5173/");
  await page.goto(
    "http://localhost:5173/reset-password?token=00000000000000000000000000000000",
  );
  await page
    .getByRole("textbox", { name: "New password:", exact: true })
    .fill("r9$CbHEaGXLUsP");
  await page
    .getByRole("textbox", { name: "Confirm new password:" })
    .fill("r9$CbHEaGXLUsP");
  await page.getByRole("button", { name: "Reset password" }).click();

  await expect(
    page.getByText("Invalid or expired password reset link"),
  ).toBeVisible();
  await expect(page).toHaveScreenshot();
});

// New password

test("should display error message when new password is null", async ({
  page,
}) => {
  await page.goto(
    "http://localhost:5173/reset-password?token=00000000000000000000000000000000",
  );
  await page
    .getByRole("textbox", { name: "Confirm new password:" })
    .fill("r9$CbHEaGXLUsP");
  await page.getByRole("button", { name: "Reset password" }).click();

  await expect(page.getByText("This field is required.")).toBeVisible();
  await expect(page).toHaveScreenshot();
});

test("should display error message when new password is too short", async ({
  page,
}) => {
  await page.goto(
    "http://localhost:5173/reset-password?token=00000000000000000000000000000000",
  );
  // 13 characters
  await page
    .getByRole("textbox", { name: "New password:", exact: true })
    .fill("grxnqdgnsqbqj");
  await page
    .getByRole("textbox", { name: "Confirm new password:" })
    .fill("grxnqdgnsqbqj");
  await page.getByRole("button", { name: "Reset password" }).click();

  await expect(
    page.getByText("Password must be at least 14 characters long."),
  ).toBeVisible();
  await expect(page).toHaveScreenshot();
});

test("should display error message when new password is too long", async ({
  page,
}) => {
  await page.goto(
    "http://localhost:5173/reset-password?token=00000000000000000000000000000000",
  );
  // 65 characters
  await page
    .getByRole("textbox", { name: "New password:", exact: true })
    .fill("metyjwgaqakvjdrbpqsoywhrqzpesbrtsbtqfseffbivpfsaaihttjnjbmrbexbpe");
  await page
    .getByRole("textbox", { name: "Confirm new password:" })
    .fill("metyjwgaqakvjdrbpqsoywhrqzpesbrtsbtqfseffbivpfsaaihttjnjbmrbexbpe");
  await page.getByRole("button", { name: "Reset password" }).click();

  await expect(
    page.getByText("Password must be at most 64 characters long."),
  ).toBeVisible();
  await expect(page).toHaveScreenshot();
});

// Confirm new password

test("should display error message when confirm new password is null", async ({
  page,
}) => {
  await page.goto(
    "http://localhost:5173/reset-password?token=00000000000000000000000000000000",
  );
  await page
    .getByRole("textbox", { name: "New password:", exact: true })
    .fill("r9$CbHEaGXLUsP");
  await page.getByRole("button", { name: "Reset password" }).click();

  await expect(page.getByText("This field is required.")).toBeVisible();
  await expect(page).toHaveScreenshot();
});

test("should display error message when confirm new password does not match", async ({
  page,
}) => {
  await page.goto(
    "http://localhost:5173/reset-password?token=00000000000000000000000000000000",
  );
  await page
    .getByRole("textbox", { name: "New password:", exact: true })
    .fill("r9$CbHEaGXLUsP");
  await page
    .getByRole("textbox", { name: "Confirm new password:" })
    .fill("r9$CbHEaGXLUsQ");
  await page.getByRole("button", { name: "Reset password" }).click();

  await expect(page.getByText("Passwords do not match.")).toBeVisible();
  await expect(page).toHaveScreenshot();
});

test("should display error message when new password is compromised", async ({
  page,
}) => {
  const email = `antanas+${uuidv4()}@inbox.lt`;

  // Sign up and verify
  await page.goto("http://localhost:5173/");
  await page.getByRole("link", { name: "Sign up" }).click();
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page
    .getByRole("textbox", { name: "Password:", exact: true })
    .fill("r9$CbHEaGXLUsP");
  await page
    .getByRole("textbox", { name: "Confirm password:" })
    .fill("r9$CbHEaGXLUsP");
  await page.getByRole("button", { name: "Submit" }).click();
  await expect(
    page.getByText("Check your email to verify the account."),
  ).toBeVisible();

  await page.goto("http://localhost:8025");
  await page.getByRole("link", { name: email }).click();
  const verifyPopupPromise = page.waitForEvent("popup");
  await page
    .locator("#preview-html")
    .contentFrame()
    .getByRole("link", { name: "http://localhost:8080/verify?" })
    .click();
  const verifyPopup = await verifyPopupPromise;
  await verifyPopup.waitForURL("**/verification-success");

  // Request password reset and extract token via API
  await page.goto("http://localhost:5173/forgot-password");
  await page.getByRole("textbox", { name: "Email:" }).fill(email);
  await page.getByRole("button", { name: "Send reset link" }).click();
  await expect(
    page.getByText("If an account with that email exists"),
  ).toBeVisible();

  const messagesResponse = await page.request.get(
    "http://localhost:8025/api/v1/messages",
  );
  const messages = await messagesResponse.json();
  const resetMessage = messages.messages.find(
    (m: { To: { Address: string }[]; Subject: string }) =>
      m.To.some((t) => t.Address === email) &&
      m.Subject === "Password Reset Request",
  );
  const messageResponse = await page.request.get(
    `http://localhost:8025/api/v1/message/${resetMessage.ID}`,
  );
  const messageData = await messageResponse.json();
  const tokenMatch = (messageData.HTML as string).match(/token=([a-f0-9]{32})/);
  const token = tokenMatch![1];

  // Submit compromised password with the real token
  await page.goto(`http://localhost:5173/reset-password?token=${token}`);
  await page
    .getByRole("textbox", { name: "New password:", exact: true })
    .fill("12345678912345");
  await page
    .getByRole("textbox", { name: "Confirm new password:" })
    .fill("12345678912345");
  await page.getByRole("button", { name: "Reset password" }).click();

  await expect(
    page.getByText(
      "The provided password is compromised and cannot be used. Use something more unique",
    ),
  ).toBeVisible();
  await expect(page).toHaveScreenshot();
});
