import { Route, Routes } from "react-router-dom";
import About from "./components/About";
import Books from "./components/Books";
import Categories from "./components/Categories";
import ErrorPage from "./components/ErrorPage";
import FavoriteBooks from "./components/FavoriteBooks";
import Footer from "./components/Footer";
import Home from "./components/Home";
import Login from "./components/Login";
import Navbar from "./components/Navbar";
import OAuth2RedirectHandler from "./components/OAuth2RedirectHandler";
import ReservedBooks from "./components/ReservedBooks";
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
            path="/categories"
            element={<Categories />}
          />
          <Route
            path="/books"
            element={<Books />}
          />
          <Route
            path="/favorite"
            element={<FavoriteBooks />}
          />
          <Route
            path="/reserved"
            element={<ReservedBooks />}
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
            path="/signup"
            element={<Signup />}
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
