import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

export default function Home() {
  const [user, setUser] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      const response = await fetch("http://localhost:8080/user", {
        method: "GET",
        credentials: "include",
      });

      const result = await response.text();

      setUser(result);
    };

    fetchData();
  }, []);

  return (
    <>
      {user ? (
        <p>Welcome {user}</p>
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
