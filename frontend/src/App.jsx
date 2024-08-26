import { Route, Routes } from "react-router-dom";
import About from "./components/About";
import ErrorPage from "./components/ErrorPage";
import Footer from "./components/Footer";
import Home from "./components/Home";
import Login from "./components/Login";
import Navbar from "./components/Navbar";
import OAuth2RedirectHandler from "./components/OAuth2RedirectHandler";

function App() {
  return (
    <>
      <Navbar />
      <main className="container">
        <Routes>
          <Route
            path="/"
            element={<Home />}
          />
          <Route
            path="/about"
            element={<About />}
          />
          <Route
            path="*"
            element={<ErrorPage />}
          />
          <Route
            path="/login"
            element={<Login />}
          />
          <Route
            path="/oauth2/redirect"
            element={<OAuth2RedirectHandler />}
          />
        </Routes>
      </main>
      <Footer />
    </>
  );
}

export default App;
