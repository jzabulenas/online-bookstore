import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch("http://localhost:8080/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          username: username,
          password: password,
        }),
      });

      const data = await response.json();

      // If login is successful, handle storing the token or session information
      // e.g., store token in localStorage
      localStorage.setItem("username", username);
      localStorage.setItem("password", password);
      localStorage.setItem("role", data.authorities[0].authority);

      // Redirect or update UI to indicate successful login
      navigate("/books");
    } catch (error) {
      // Handle login errors (e.g., show error message)
      console.error("Login failed:", error);
    }
  };

  return (
    <div className="container">
      <div className="row">
        <div className="col">
          <form onSubmit={handleLogin}>
            <input
              type="text"
              placeholder="Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
            <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            <button type="submit">Login</button>
          </form>
        </div>
      </div>
    </div>
  );
}
