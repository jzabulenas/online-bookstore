import { Link, NavLink } from "react-router-dom";
import logo from "../assets/logo.png";
import "./Navbar.css";

export default function Navbar() {
  // const location = useLocation();
  // const navigate = useNavigate();
  const roles = JSON.parse(sessionStorage.getItem("roles"));

  // useEffect(() => {
  //   const cleanedPathname = location.pathname.replace(/\/\/+/g, "/");

  //   if (location.pathname !== cleanedPathname) {
  //     navigate(cleanedPathname, { replace: true });
  //   }
  // }, [location.pathname, navigate]);

  const logout = () => {
    localStorage.removeItem("role");
    localStorage.removeItem("username");
    localStorage.removeItem("password");
    navigate("/");
    handleLinkClick();
  };

  const handleLinkClick = () => {
    // Get the navbar collapse element
    const navbarCollapse = document.querySelector(".navbar-collapse");

    // Toggle the 'show' class on the navbar collapse element to hide it
    navbarCollapse.classList.remove("show");
  };

  return (
    <nav className="navbar navbar-expand-md bg-body-tertiary mb-3">
      <div className="container">
        <Link
          className="navbar-brand"
          to={"/"}
        >
          <img
            src={logo}
            alt="Book recommendation app logo"
            className="logo"
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
          <ul className="navbar-nav ms-auto mb-2 mb-lg-0">
            <li className="nav-item">
              <NavLink
                className="nav-link"
                to="/"
                onClick={handleLinkClick}
              >
                Home
              </NavLink>
            </li>

            {role === "ADMIN" && (
              <li className="nav-item">
                <NavLink
                  className="nav-link"
                  to={"/categories"}
                  onClick={handleLinkClick}
                >
                  Categories
                </NavLink>
              </li>
            )}

            {(role === "ADMIN" || role === "USER") && (
              <li className="nav-item">
                <NavLink
                  className="nav-link"
                  to={"/books"}
                  onClick={handleLinkClick}
                >
                  Books
                </NavLink>
              </li>
            )}

            {role === "USER" && (
              <>
                <li className="nav-item">
                  <NavLink
                    className="nav-link"
                    to={"/favorite"}
                    onClick={handleLinkClick}
                  >
                    Favorite Books
                  </NavLink>
                </li>

                <li className="nav-item">
                  <NavLink
                    className="nav-link"
                    to={"/reserved"}
                    onClick={handleLinkClick}
                  >
                    Reserved Books
                  </NavLink>
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
              </>
            )}

            {role !== "USER" && role !== "ADMIN" && (
              <>
                <li className="nav-item">
                  <NavLink
                    className="nav-link"
                    to={"/login"}
                    onClick={handleLinkClick}
                  >
                    Log in
                  </NavLink>
                </li>

                <li className="nav-item">
                  <NavLink
                    className="nav-link"
                    to={"/signup"}
                    onClick={handleLinkClick}
                  >
                    Sign up
                  </NavLink>
                </li>

                <li className="nav-item">
                  <NavLink
                    className="nav-link"
                    to={"/about"}
                    onClick={handleLinkClick}
                  >
                    About
                  </NavLink>
                </li>
              </>
            )}

            {(role === "ADMIN" || role === "USER") && (
              <li className="nav-item">
                <button
                  className="nav-link"
                  onClick={logout}
                >
                  Log out
                </button>
              </li>
            )}
          </ul>
        </div>
      </div>
    </nav>
  );
}
