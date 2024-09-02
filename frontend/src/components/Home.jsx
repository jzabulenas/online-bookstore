import { Link } from "react-router-dom";
import useSessionStorage from "../hooks/useSessionStorage";
import GenerateBooks from "./GenerateBooks";

export default function Home() {
  const email = sessionStorage.getItem("email");
  // I do this so a render would happen when roles
  // are removed from session storage.
  const roles = useSessionStorage("roles");

  return (
    <>
      {email ? (
        <>
          <p>Welcome {email}</p>

          <GenerateBooks />
        </>
      ) : (
        <div className="text-center p-5">
          <h1 className="fw-bold text-break">Book recommendation app</h1>
          <h2 className="fs-5 text-black text-opacity-75">
            Easily find new books to read
          </h2>
          <Link to="/about">Learn more</Link>
        </div>
      )}
    </>
  );
}
