import { test as setup, devices, expect } from "@playwright/test";

setup.use({
  ...devices["Pixel 5"],
});

setup("create default user", async ({ page }) => {
  console.log("---");
  console.log("create default user...");
  console.log("---");

  // Sign up
  await page.goto("http://localhost:5173/");
  await page.getByRole("button", { name: "Toggle navigation" }).tap();
  await page.locator(".navbar-collapse.collapse.show").waitFor();
  await page.getByRole("link", { name: "Sign up" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).tap();
  await page.getByRole("textbox", { name: "Email:" }).fill("jurgis@inbox.lt");
  await page.getByRole("textbox", { name: "Password:", exact: true }).tap();
  await page
    .getByRole("textbox", { name: "Password:", exact: true })
    .fill("JfRn9Lb97*qs!#");
  await page.getByRole("textbox", { name: "Confirm password:" }).tap();
  await page
    .getByRole("textbox", { name: "Confirm password:" })
    .fill("JfRn9Lb97*qs!#");
  await page.getByRole("button", { name: "Submit" }).tap();

  await expect(page).toHaveURL("http://localhost:5173/");
  await expect(
    page.getByText("Check your email to verify the account.")
  ).toBeVisible();
  await expect(page.getByRole("alert")).toContainText(
    "Check your email to verify the account."
  );
  await expect(page).toHaveScreenshot();

  // Verify email
  await page.goto("http://localhost:8025");
  await page.getByRole("link", { name: "noreply@myapp.xyz" }).tap();
  await page
    .locator("#preview-html")
    .contentFrame()
    .getByRole("link", { name: "http://localhost:8080/verify?" })
    .tap();
  await page.goto("http://localhost:8025");
  await page.getByRole("link", { name: "noreply@myapp.xyz" }).tap();
  // Clicks on the trash icon logo to delete the email. Not sure how this 
  // signifies a trash can
  await page.getByRole("button", { name: "" }).tap();
  await expect(page).toHaveScreenshot();
});

// The solution below works too, though I am not sure if it is a more proper way, considering you can just use Playwright to do it, as above

// setup("create new database", async ({ page }) => {
//   console.log("---");
//   console.log("creating new database...");
//   console.log("---");
//   // Initialize the database

//   const context = await request.newContext();

//   // Step 1: Get CSRF token from GET /open
//   const res = await context.get("http://localhost:8080/open");
//   const cookies = await context.storageState();

//   const csrfCookie = cookies.cookies.find((c) => c.name === "XSRF-TOKEN");
//   const csrfToken = csrfCookie?.value;

//   if (!csrfToken) {
//     throw new Error("CSRF token not found in cookies");
//   }

//   // Step 2: Use token in POST request
//   const signupResponse = await context.post("http://localhost:8080/signup", {
//     headers: {
//       "Content-Type": "application/json",
//       "X-XSRF-TOKEN": csrfToken,
//     },
//     data: {
//       email: "jurgis@inbox.lt",
//       password: "123456",
//       roles: [1],
//     },
//   });

//   if (signupResponse.status() !== 200) {
//     throw new Error(`Signup failed: ${signupResponse.status()}`);
//   }

//   await context.dispose();
// });
