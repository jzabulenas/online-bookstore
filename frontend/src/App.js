import Navbar from "./components/Navbar";
import { Route, Routes } from "react-router-dom";
import Home from "./components/Home";
import Categories from "./components/Categories";
import Books from "./components/Books";
import FavoriteBooks from "./components/FavoriteBooks";
import ReservedBooks from "./components/ReservedBooks";
import About from "./components/About";
import ErrorPage from "./components/ErrorPage";

function App() {
  return (
    <>
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
        </Routes>
      </main>
    </>
  );
}

export default App;
