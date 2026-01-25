import { Link } from "react-router-dom";
import useLocalStorage from "../hooks/useLocalStorage";
import GenerateBooks from "./GenerateBooks";
import { useEffect } from "react";

export default function Home({ isSignedUp, setIsSignedUp }) {
  const email = localStorage.getItem("email");

  // I do this so a render would happen when roles
  // are removed from session storage.
  const roles = useLocalStorage("roles");

  // Using this for receiving CSRF token
  useEffect(() => {
    async function getToken() {
      const url = `${import.meta.env.VITE_BACKEND_BASE_URL}/open`;

      try {
        const response = await fetch(url, {
          method: "GET",
          credentials: "include", // Apparently this is required here, even though I do not send any data
        });

        if (!response.ok) {
          throw new Error(`Response status: ${response.status}`);
        }
      } catch (error) {
        console.error(error.message);
        // sessionStorage.clear();
        // navigate("/login");
      }
    }

    getToken();
  }, []);

  return (
    <>
      {email ? (
        <>
          <h1 className="mb-4">Welcome, {email}</h1>
          <h2>How to use</h2>
          <p className="mb-4">
            For best results, try inputting the whole book title, with author.
            For example, Pride and Prejudice by Jane Austen.
          </p>

          <GenerateBooks />
        </>
      ) : (
        <>
          {isSignedUp && (
            <div
              className="alert alert-success alert-dismissible"
              role="alert"
            >
              Check your email to verify the account.
              <button
                type="button"
                className="btn-close"
                aria-label="Close"
                onClick={() => setIsSignedUp(false)}
              ></button>
            </div>
          )}
          <div className="text-center p-5">
            <h1 className="fw-bold text-break mb-3">Book recommendation app</h1>
            <h2 className="fs-5 text-black text-opacity-75 mb-3">
              Easily find new books to read using AI
            </h2>
            <Link to="/about">Learn more</Link>
          </div>
        </>
      )}
    </>
  );
}
