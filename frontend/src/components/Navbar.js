import logo from "../assets/logo.png";
import { Link } from "react-router-dom";
import { useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";

export default function Navbar() {
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    const cleanedPathname = location.pathname.replace(/\/\/+/g, "/");

    if (location.pathname !== cleanedPathname) {
      navigate(cleanedPathname, { replace: true });
    }
  }, [location.pathname, navigate]);

  return (
    <nav className="navbar navbar-expand-lg bg-body-tertiary">
      <div className="container-fluid">
        <Link
          className="navbar-brand"
          to={"/"}
        >
          <img
            src={logo}
            alt="Book Reservation App Logo"
            style={{ width: "40px" }}
          />
        </Link>
        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarSupportedContent"
          aria-controls="navbarSupportedContent"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>
        <div
          className="collapse navbar-collapse"
          id="navbarSupportedContent"
        >
          <ul className="navbar-nav me-auto mb-2 mb-lg-0 w-100 justify-content-between">
            <li className="nav-item">
              <Link
                className="nav-link"
                to={"/categories"}
              >
                Categories
              </Link>
            </li>
            <li className="nav-item">
              <Link
                className="nav-link"
                to={"/books"}
              >
                Books
              </Link>
            </li>
            <li className="nav-item">
              <Link
                className="nav-link"
                to={"/favorite"}
              >
                Favorite Books
              </Link>
            </li>
            <li className="nav-item">
              <Link
                className="nav-link"
                to={"/reserved"}
              >
                Reserved Books
              </Link>
            </li>
            <li className="nav-item">
              <Link
                className="nav-link"
                to={"/about"}
              >
                About
              </Link>
            </li>
            <form
              className="d-flex"
              role="search"
            >
              <input
                className="form-control me-2"
                type="search"
                name="search"
                placeholder="Search"
                aria-label="Search"
              />
              <button
                className="btn btn-outline-success"
                type="submit"
              >
                Search
              </button>
            </form>
          </ul>
        </div>
      </div>
    </nav>
  );
}
