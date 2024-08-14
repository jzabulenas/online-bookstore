import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

export default function OAuth2RedirectHandler() {
  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      const response = await fetch("http://localhost:8080/user", {
        method: "GET",
        credentials: "include",
      });

      if (response.ok) {
        const result = await response.text();
        sessionStorage.setItem("user", result);
        navigate("/"); // Redirect to home
      } else {
        console.error("Failed to fetch user data");
        navigate("/login"); // Redirect back to login if needed
      }
    };

    fetchData();
  }, [navigate]);

  return <p>Redirecting...</p>;
}