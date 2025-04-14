import { Route, Routes } from "react-router-dom";
import About from "./components/About";
import ErrorPage from "./components/ErrorPage";
import Footer from "./components/Footer";
import Home from "./components/Home";
import Login from "./components/Login";
import Navbar from "./components/Navbar";
import OAuth2RedirectHandler from "./components/OAuth2RedirectHandler";
import SavedBooks from "./components/SavedBooks";
import Signup from "./components/Signup";

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
          <Route
            path="/saved-books"
            element={<SavedBooks />}
          />
          <Route
            path="/signup"
            element={<Signup />}
          />
        </Routes>
      </main>
      <Footer />
    </>
  );
}

export default App;
