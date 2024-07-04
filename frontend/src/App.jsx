import Navbar from "./components/Navbar";
import { Route, Routes } from "react-router-dom";
import Home from "./components/Home";
import Categories from "./components/Categories";
import Books from "./components/Books";
import FavoriteBooks from "./components/FavoriteBooks";
import ReservedBooks from "./components/ReservedBooks";
import About from "./components/About";
import ErrorPage from "./components/ErrorPage";
import Footer from "./components/Footer";
import Login from "./components/Login";
import Signup from "./components/Signup";

function App() {
  return (
    <div className="d-flex flex-column min-vh-100">
      <Navbar />
      <main>
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
        </Routes>
      </main>
      <Footer />
    </div>
  );
}

export default App;
